package javax.persistence.filter.core;

import static javax.persistence.filter.core.VolatilePath.ROOT_PREFIX;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.exception.OffsetOutOfRangeException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Michel Risucci
 */
public class Filters {

	private static final Log log = LogFactory.getLog(Filter.class);

	private static final String LISTING = "Filtering entity %s, found %d entries.";
	private static final String COUNTING = "Counting entity %s, found %d entries.";

	private static final String DISTINCT = "DISTINCT(" + ROOT_PREFIX + ") ";
	private static final String COUNT = "COUNT(" + ROOT_PREFIX + ") ";
	private static final String COUNT_DISTINCT = "COUNT(DISTINCT(" + ROOT_PREFIX + ")) ";

	/**
	 * Returns JPA entity name.
	 * 
	 * @param type
	 * @return
	 */
	private static <E> String getEntityName(Class<E> type) {
		Entity entity = type.getAnnotation(Entity.class);
		if (entity != null && !entity.name().isEmpty()) {
			return entity.name();
		} else {
			return type.getSimpleName();
		}
	}

	/**
	 * @param em
	 * @param filter
	 * @return
	 */
	public static <E> PageFilter<E> filter(EntityManager em, Filter<E> filter) {
		return filter(em, filter, -1, -1);
	}

	/**
	 * @param em
	 * @param filter
	 * @param offset
	 * @param limit
	 * @return
	 * @throws OffsetOutOfRangeException
	 */
	public static <E> PageFilter<E> filter(EntityManager em, Filter<E> filter, int offset, int limit)
			throws OffsetOutOfRangeException {

		Class<E> type = filter.getRootType();

		long count = count(em, filter);
		log.info(String.format(COUNTING, getEntityName(type), count));
		checkResultOutOfRange(count, offset);

		List<E> list = list(em, filter, offset, limit);
		log.info(String.format(LISTING, getEntityName(type), list.size()));

		return new PageFilter<E>(list, limit, count);
	}

	/**
	 * @param em
	 * @param filter
	 * @return
	 */
	public static <E> long count(EntityManager em, Filter<E> filter) {

		Class<E> type = filter.getRootType();
		String entityName = getEntityName(type);
		Map<String, String> joinAliases = new HashMap<String, String>();

		StringBuilder b = new StringBuilder() //
				.append("SELECT ") //
				.append(filter.isDistinct() ? COUNT_DISTINCT : COUNT) //
				.append("FROM " + entityName + " " + ROOT_PREFIX + " ");

		List<Where> wheres = filter.getWheres();
		boolean existWheres = wheres != null && !wheres.isEmpty();
		if (existWheres) {
			buildJpqlWhereParams(b, joinAliases, wheres);
		}

		String jpql = b.toString();
		log.info(jpql);

		TypedQuery<Number> query = em.createQuery(jpql, Number.class);
		if (existWheres) {
			setQueryWhereParams(query, wheres);
		}

		return query.getSingleResult().longValue();
	}

	/**
	 * @param em
	 * @param filter
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static <E> List<E> list(EntityManager em, Filter<E> filter, int offset, int limit) {

		Class<E> type = filter.getRootType();
		String entityName = getEntityName(type);
		Map<String, String> joinAliases = new HashMap<String, String>();

		StringBuilder b = new StringBuilder() //
				.append("SELECT ") //
				.append(filter.isDistinct() ? DISTINCT : ROOT_PREFIX + " ") //
				.append("FROM " + entityName + " " + ROOT_PREFIX + " ");

		List<Where> wheres = filter.getWheres();
		boolean existWheres = wheres != null && !wheres.isEmpty();
		if (existWheres) {
			buildJpqlWhereParams(b, joinAliases, wheres);
		}

		List<Order> orders = filter.getOrders();
		boolean existOrders = orders != null && !orders.isEmpty();
		if (existOrders) {
			buildOrderParams(b, joinAliases, orders);
		}

		String jpql = b.toString();
		log.info(jpql);

		TypedQuery<E> query = em.createQuery(jpql, filter.getRootType());
		if (existWheres) {
			setQueryWhereParams(query, wheres);
		}
		return setQueryFetchRange(query, limit, offset).getResultList();
	}

	/**
	 * @param b
	 * @param aliases
	 * @param wheres
	 */
	private static void buildJpqlWhereParams(StringBuilder b, Map<String, String> aliases, List<Where> wheres) {

		String joins = "";
		for (ListIterator<Where> i = wheres.listIterator(); i.hasNext();) {
			Where where = i.next();
			joins = where.processJoins(aliases);
		}
		b.append(joins);

		b.append("WHERE ");
		for (ListIterator<Where> i = wheres.listIterator(); i.hasNext();) {
			Where where = i.next();
			b.append(where.getClause());

			if (i.hasNext()) {
				b.append("AND");
			}
			b.append(" ");
		}
	}

	/**
	 * @param b
	 * @param aliases
	 * @param orders
	 */
	private static void buildOrderParams(StringBuilder b, Map<String, String> aliases, List<Order> orders) {
		b.append("ORDER BY ");
		for (ListIterator<Order> i = orders.listIterator(); i.hasNext();) {
			Order order = i.next();
			b.append(order.getClause());

			if (i.hasNext()) {
				b.append(",");
			}
			b.append(" ");
		}
	}

	/**
	 * @param query
	 * @param wheres
	 */
	private static <E> void setQueryWhereParams(TypedQuery<E> query, List<Where> wheres) {
		for (Where where : wheres) {
			where.compileClause(query);
		}
	}

	/**
	 * @param query
	 * @param limit
	 * @param offset
	 * @return
	 */
	private static <E> TypedQuery<E> setQueryFetchRange(TypedQuery<E> query, int limit, int offset) {
		// Fetching without limit
		if (limit == -1 && offset == -1) {
			return query;
		}
		// Limit fetching
		else {
			return query.setFirstResult(offset).setMaxResults(limit);
		}
	}

	/**
	 * @param count
	 * @param offset
	 */
	private static void checkResultOutOfRange(long count, int offset) {
		if ((count == 0 && offset != 0) || (count > 0 && offset > count)) {
			throw new OffsetOutOfRangeException(count, offset);
		}
	}

}