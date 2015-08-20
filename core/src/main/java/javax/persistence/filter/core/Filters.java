package javax.persistence.filter.core;

import static javax.persistence.filter.core.VolatilePath.ROOT_PREFIX;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
		Set<String> aliases = new HashSet<String>();

		StringBuilder b = new StringBuilder() //
				.append("SELECT ") //
				.append(filter.isDistinct() ? COUNT_DISTINCT : COUNT) //
				.append("FROM " + entityName + " " + ROOT_PREFIX + " ");

		Set<Where> wheres = filter.getWheres();
		boolean existWheres = wheres != null && !wheres.isEmpty();

		// Processing junctions
		String junctions = processJunctions(wheres, null, aliases);
		b.append(junctions);

		if (existWheres) {
			buildJpqlWhereParams(b, wheres);
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
		Set<String> aliases = new HashSet<String>();

		StringBuilder b = new StringBuilder() //
				.append("SELECT ") //
				.append(filter.isDistinct() ? DISTINCT : ROOT_PREFIX + " ") //
				.append("FROM " + entityName + " " + ROOT_PREFIX + " ");

		Set<Where> wheres = filter.getWheres();
		boolean existWheres = wheres != null && !wheres.isEmpty();
		Set<Order> orders = filter.getOrders();
		boolean existOrders = orders != null && !orders.isEmpty();

		// Processing junctions
		String junctions = processJunctions(wheres, orders, aliases);
		b.append(junctions);

		if (existWheres) {
			buildJpqlWhereParams(b, wheres);
		}
		if (existOrders) {
			buildOrderParams(b, orders);
		}

		String jpql = b.toString();
		log.info(jpql);

		TypedQuery<E> query = em.createQuery(jpql, filter.getRootType());
		if (existWheres) {
			setQueryWhereParams(query, wheres);
		}
		return setQueryFetchRange(query, limit, offset).getResultList();
	}

	private static <E> String processJunctions(Set<Where> wheres, Set<Order> orders, Set<String> aliases) {

		Set<VolatilePath> paths = new HashSet<VolatilePath>();
		if (wheres != null) {
			paths.addAll(wheres);
		}
		if (orders != null) {
			paths.addAll(orders);
		}

		StringBuilder joins = new StringBuilder();
		for (Iterator<VolatilePath> i = paths.iterator(); i.hasNext();) {
			VolatilePath path = i.next();
			joins.append(path.processAliases(aliases));
		}
		return joins.toString();
	}

	/**
	 * @param b
	 * @param wheres
	 */
	private static void buildJpqlWhereParams(StringBuilder b, Set<Where> wheres) {
		b.append("WHERE ");
		for (Iterator<Where> i = wheres.iterator(); i.hasNext();) {
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
	 * @param orders
	 */
	private static void buildOrderParams(StringBuilder b, Set<Order> orders) {
		b.append("ORDER BY ");
		for (Iterator<Order> i = orders.iterator(); i.hasNext();) {
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
	private static <E> void setQueryWhereParams(TypedQuery<E> query, Set<Where> wheres) {
		for (Iterator<Where> i = wheres.iterator(); i.hasNext();) {
			i.next().compileClause(query);
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