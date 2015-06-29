package javax.persistence.filter.core;

import java.util.List;
import java.util.ListIterator;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.exception.FirstResultOutOfRangeException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Michel Risucci
 */
public class Filters {

	private static final Logger log = LogManager.getLogger(Filters.class);

	private static final String FIRST_RESULT_OUT_OF_RANGE = "Start position \"%d\" to this filter is not the first (equals to 0) and found nothing: possibly out of pagination range.";
	private static final String LISTING = "Filtering entity %s, found %d entries.";
	private static final String COUNTING = "Counting entity %s, found %d entries.";

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
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws FirstResultOutOfRangeException
	 */
	public static <E> PageFilter<E> filter( //
			EntityManager entityManager, //
			Filter<E> filter, //
			int firstResult, //
			int maxResults) throws FirstResultOutOfRangeException {

		Class<E> type = filter.getRootType();

		long count = count(entityManager, filter);
		log.info(String.format(COUNTING, getEntityName(type), count));

		if ((count == 0 && firstResult != 0)
				|| (count > 0 && firstResult > count)) {
			throw new FirstResultOutOfRangeException( //
					String.format(FIRST_RESULT_OUT_OF_RANGE, firstResult));
		}

		List<E> list = list(entityManager, filter, firstResult, maxResults);
		log.info(String.format(LISTING, getEntityName(type), list.size()));

		return new PageFilter<E>(list, maxResults, count);
	}

	/**
	 * @param entityManager
	 * @param filter
	 * @return
	 */
	public static <E> long count( //
			EntityManager entityManager, //
			Filter<E> filter) {

		Class<E> type = filter.getRootType();
		String entityName = getEntityName(type);

		StringBuilder b = new StringBuilder() //
				.append("SELECT COUNT(x) ") //
				.append("FROM " + entityName + " x ");

		List<Where> wheres = filter.getWheres();
		boolean existWheres = wheres != null && !wheres.isEmpty();
		if (existWheres) {
			buildJpqlWhereParams(b, wheres);
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
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public static <E> List<E> list( //
			EntityManager entityManager, //
			Filter<E> filter, //
			int firstResult, //
			int maxResults) {

		Class<E> type = filter.getRootType();
		String entityName = getEntityName(type);

		StringBuilder b = new StringBuilder() //
				.append("SELECT x ") //
				.append("FROM " + entityName + " x ");

		List<Where> wheres = filter.getWheres();
		boolean existWheres = wheres != null && !wheres.isEmpty();
		if (existWheres) {
			buildJpqlWhereParams(b, wheres);
		}

		List<Order> orders = filter.getOrders();
		boolean existOrders = orders != null && !orders.isEmpty();
		if (existOrders) {
			buildOrderParams(b, orders);
		}

		String jpql = b.toString();
		log.info(jpql);

		TypedQuery<E> query = entityManager //
				.createQuery(jpql, filter.getRootType());

		if (existWheres) {
			setQueryWhereParams(query, wheres);
		}

		return query //
				.setFirstResult(firstResult) //
				.setMaxResults(maxResults) //
				.getResultList();
	}

	/**
	 * @param volatilePath
	 * @return
	 */
	private static String getAliasRealPath(VolatilePath volatilePath) {
		// TODO Adds alias and JOINS functionality.
		return "x." + volatilePath.getPath();
	}

	/**
	 * @param b
	 * @param wheres
	 */
	private static void buildJpqlWhereParams(StringBuilder b, List<Where> wheres) {
		b.append("WHERE ");
		for (ListIterator<Where> i = wheres.listIterator(); i.hasNext();) {
			Where where = i.next();
			String aliasRealPath = getAliasRealPath(where);
			b.append(where.getClause(aliasRealPath));

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
	private static void buildOrderParams(StringBuilder b, List<Order> orders) {
		b.append("ORDER BY ");
		for (ListIterator<Order> i = orders.listIterator(); i.hasNext();) {
			Order order = i.next();
			String aliasRealPath = getAliasRealPath(order);
			b.append(order.getClause(aliasRealPath));

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