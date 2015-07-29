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
import javax.persistence.filter.exception.FirstResultOutOfRangeException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Michel Risucci
 */
public class Filters {

	private static final Log log = LogFactory.getLog(Filter.class);

	private static final String FIRST_RESULT_OUT_OF_RANGE = "Start position \"%d\" to this filter is not the first (equals to 0) and found nothing: possibly out of pagination range.";
	private static final String LISTING = "Filtering entity %s, found %d entries.";
	private static final String COUNTING = "Counting entity %s, found %d entries.";

	private static final String DISTINCT = "DISTINCT(" + ROOT_PREFIX + ") ";
	private static final String COUNT = "COUNT(" + ROOT_PREFIX + ") ";
	private static final String COUNT_DISTINCT = "COUNT(DISTINCT("
			+ ROOT_PREFIX + ")) ";

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
	 * @param entityManager
	 * @param filter
	 * @param offset
	 * @param limit
	 * @return
	 * @throws FirstResultOutOfRangeException
	 */
	public static <E> PageFilter<E> filter( //
			EntityManager entityManager, //
			Filter<E> filter, //
			int offset, //
			int limit) throws FirstResultOutOfRangeException {

		Map<String, String> joinAliases = new HashMap<String, String>();
		Class<E> type = filter.getRootType();

		long count = count(entityManager, filter, joinAliases);
		log.info(String.format(COUNTING, getEntityName(type), count));

		if ((count == 0 && offset != 0) || (count > 0 && offset > count)) {
			throw new FirstResultOutOfRangeException( //
					String.format(FIRST_RESULT_OUT_OF_RANGE, offset));
		}

		List<E> list = list(entityManager, filter, joinAliases, offset, limit);
		log.info(String.format(LISTING, getEntityName(type), list.size()));

		return new PageFilter<E>(list, limit, count);
	}

	/**
	 * @param entityManager
	 * @param filter
	 * @param joinAliases
	 * @return
	 */
	public static <E> long count( //
			EntityManager entityManager, //
			Filter<E> filter, //
			Map<String, String> joinAliases) {

		Class<E> type = filter.getRootType();
		String entityName = getEntityName(type);

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

		TypedQuery<Number> query = entityManager //
				.createQuery(jpql, Number.class);

		if (existWheres) {
			setQueryWhereParams(query, wheres);
		}

		return query.getSingleResult().longValue();
	}

	/**
	 * @param entityManager
	 * @param filter
	 * @param joinAliases
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static <E> List<E> list( //
			EntityManager entityManager, //
			Filter<E> filter, //
			Map<String, String> joinAliases, //
			int offset, //
			int limit) {

		Class<E> type = filter.getRootType();
		String entityName = getEntityName(type);

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

		TypedQuery<E> query = entityManager //
				.createQuery(jpql, filter.getRootType());

		if (existWheres) {
			setQueryWhereParams(query, wheres);
		}

		return query //
				.setFirstResult(offset) //
				.setMaxResults(limit) //
				.getResultList();
	}

	/**
	 * @param b
	 * @param aliases
	 * @param wheres
	 */
	private static void buildJpqlWhereParams(StringBuilder b,
			Map<String, String> aliases, List<Where> wheres) {

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
	private static void buildOrderParams(StringBuilder b,
			Map<String, String> aliases, List<Order> orders) {
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
	private static <E> void setQueryWhereParams(TypedQuery<E> query,
			List<Where> wheres) {
		for (Where where : wheres) {
			where.compileClause(query);
		}
	}

}