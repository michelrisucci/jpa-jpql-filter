package javax.persistence.filter.core;

import static javax.persistence.filter.core.VolatilePath.ROOT_PREFIX;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.exception.OffsetOutOfRangeException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JPA JPQL utility class that performs JPQL queries logic based on
 * {@link Filter} instance.
 * 
 * @author Michel Risucci
 */
public class Filters {

	private static final Log log = LogFactory.getLog(Filter.class);

	private static final String LISTING = "Filtering entity %s, found %d entries.";
	private static final String COUNTING = "Counting entity %s, found %d entries.";

	private static final String DISTINCT = "DISTINCT " + ROOT_PREFIX + " ";
	private static final String COUNT = "COUNT( " + ROOT_PREFIX + " ) ";
	private static final String COUNT_DISTINCT = "COUNT( DISTINCT " + ROOT_PREFIX + " ) ";

	/**
	 * Lists all entries of an entity.
	 * 
	 * @param <E>
	 *            dynamic entity Java Class type
	 * @param em
	 *            {@link EntityManager}
	 * @param type
	 *            Java Entity Class
	 * @return selection of all table contents
	 */
	public static <E> List<E> listAll(EntityManager em, Class<E> type) {
		return list(em, Filter.newInstance(type), -1, -1);
	}

	/**
	 * Counts all entries of an entity.
	 * 
	 * @param <E>
	 *            dynamic entity Java Class type
	 * @param em
	 *            {@link EntityManager}
	 * @param type
	 *            Java Entity Class
	 * @return count of all table contents
	 */
	public static <E> long countAll(EntityManager em, Class<E> type) {
		return count(em, Filter.newInstance(type));
	}

	/**
	 * JPA JPQL Filter main method that performs the query logic based on
	 * {@link Filter} instance, ignoring the fetch range.
	 * 
	 * @param <E>
	 *            dynamic entity Java Class type
	 * @param em
	 *            {@link EntityManager}
	 * @param filter
	 *            {@link Filter} instance
	 * @return selection of filtered results without pagination
	 */
	public static <E> PageFilter<E> filter(EntityManager em, Filter<E> filter) {
		return filter(em, filter, -1, -1);
	}

	/**
	 * JPA JPQL Filter main method that performs the query logic based on
	 * {@link Filter} instance.
	 * 
	 * @param <E>
	 *            dynamic entity Java Class type
	 * @param em
	 *            {@link EntityManager}
	 * @param filter
	 *            {@link Filter} instance
	 * @param offset
	 *            number of rows to skip before beginning to fetch
	 * @param limit
	 *            number of rows to fetch starting from the offset
	 * @return the selection according to the {@link Filter} instance
	 * @throws OffsetOutOfRangeException
	 *             if the offset is greater than the count
	 */
	public static <E> PageFilter<E> filter(EntityManager em, Filter<E> filter, int offset, int limit)
			throws OffsetOutOfRangeException {

		// Counting the number of results available for this filter instance.
		long count = count(em, filter);

		// Checking if offset is out of range, according to the count.
		if (offset > count) {
			throw new OffsetOutOfRangeException(count, offset);
		}

		// Listing the results available for this filter model.
		List<E> list = list(em, filter, offset, limit);
		return new PageFilter<E>(list, limit, count);
	}

	/**
	 * Filter method that performs count query based on {@link Filter} instance.
	 * 
	 * @param <E>
	 *            dynamic entity Java Class type
	 * @param em
	 *            {@link EntityManager}
	 * @param filter
	 *            {@link Filter} instance
	 * @return number of rows available for this {@link Filter} instance
	 */
	public static <E> long count(EntityManager em, Filter<E> filter) {

		// Starting query.
		String name = filter.getEntityName();
		StringBuilder b = new StringBuilder() //
				.append("SELECT ") //
				.append(filter.isDistinct() ? COUNT_DISTINCT : COUNT) //
				.append("FROM " + name + " " + ROOT_PREFIX + " ");

		// Getting where and order clauses for processing.
		Set<Where> wheres = filter.getWheres();
		Set<Order> orders = filter.getOrders();

		// Processing junctions according to clauses.
		Set<String> aliases = new HashSet<String>();
		String junctions = processJunctions(wheres, orders, aliases);
		b.append(junctions);

		// Processing where clauses.
		boolean existWheres = wheres != null && !wheres.isEmpty();
		if (existWheres) {
			buildJpqlWhereParams(b, wheres);
		}

		// Generating and logging final JPQL query.
		String jpql = b.toString();
		log.info(jpql);

		// Creating JPA query.
		TypedQuery<Number> query = em.createQuery(jpql, Number.class);
		// Adding query where parameters.
		if (existWheres) {
			setQueryWhereParams(query, wheres);
		}

		// Fetching and logging count result.
		long count = query.getSingleResult().longValue();
		log.info(String.format(COUNTING, name, count));
		return count;
	}

	/**
	 * Filter method that performs list query based on {@link Filter} instance.
	 * 
	 * @param <E>
	 *            dynamic entity Java Class type
	 * @param em
	 *            {@link EntityManager}
	 * @param filter
	 *            {@link Filter} instance
	 * @param offset
	 *            number of rows to skip before beginning to fetch
	 * @param limit
	 *            number of rows to fetch starting from the offset
	 * @return list of fetched values for this {@link Filter} instance
	 */
	public static <E> List<E> list(EntityManager em, Filter<E> filter, int offset, int limit) {

		// Starting query.
		String name = filter.getEntityName();
		StringBuilder b = new StringBuilder() //
				.append("SELECT ") //
				.append(filter.isDistinct() ? DISTINCT : ROOT_PREFIX + " ") //
				.append("FROM " + name + " " + ROOT_PREFIX + " ");

		// Getting where and order clauses for processing.
		Set<Where> wheres = filter.getWheres();
		Set<Order> orders = filter.getOrders();

		// Processing junctions according to clauses.
		Set<String> aliases = new HashSet<String>();
		String junctions = processJunctions(wheres, orders, aliases);
		b.append(junctions);

		// Processing where clauses.
		boolean existWheres = wheres != null && !wheres.isEmpty();
		if (existWheres) {
			buildJpqlWhereParams(b, wheres);
		}

		// Processing order clauses.
		boolean existOrders = orders != null && !orders.isEmpty();
		if (existOrders) {
			buildJpqlOrdering(b, orders);
		}

		// Generating and logging final JPQL query.
		String jpql = b.toString();
		log.info(jpql);

		// Creating JPA query.
		TypedQuery<E> query = em.createQuery(jpql, filter.getRootType());
		// Adding query where parameters.
		if (existWheres) {
			setQueryWhereParams(query, wheres);
		}

		// Fetching and logging list result.
		setQueryFetchRange(query, limit, offset);
		List<E> list = query.getResultList();
		log.info(String.format(LISTING, name, list.size()));
		return list;
	}

	/**
	 * Processes junctions (joins) for the {@link Where} and {@link Order}
	 * clauses, required on this {@link Filter} instance.
	 * 
	 * @param <E>
	 *            dynamic entity Java Class type
	 * @param wheres
	 *            set of {@link Where} clauses
	 * @param orders
	 *            set of {@link Order} clauses
	 * @param aliases
	 *            set of processed aliases to compare if already exists
	 * @return portion of JPQL query representing the junctions
	 */
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
	 * Builds JPQL where clauses parameters to be filled with values on
	 * {@link Filters#setQueryWhereParams(TypedQuery, Set)}.
	 * 
	 * @param b
	 *            JPQL {@link StringBuilder}
	 * @param wheres
	 *            set of {@link Where} clauses
	 */
	private static void buildJpqlWhereParams(StringBuilder b, Set<Where> wheres) {
		b.append("WHERE ");

		for (Iterator<Where> i = wheres.iterator(); i.hasNext();) {
			Where where = i.next();
			b.append(where.getJpqlClause());

			if (i.hasNext()) {
				b.append("AND ");
			}
		}
	}

	/**
	 * Builds JPQL ordering clauses.
	 * 
	 * @param b
	 *            JPQL {@link StringBuilder}
	 * @param orders
	 *            set of {@link Order} clauses
	 */
	private static void buildJpqlOrdering(StringBuilder b, Set<Order> orders) {
		b.append("ORDER BY ");
		for (Iterator<Order> i = orders.iterator(); i.hasNext();) {
			Order order = i.next();
			b.append(order.getJpqlClause());

			if (i.hasNext()) {
				b.append(",");
			}
			b.append(" ");
		}
	}

	/**
	 * Sets required values to all JPQL {@link Where} clause fields.
	 * 
	 * @param <E>
	 *            dynamic entity Java Class type
	 * @param query
	 *            JPA query
	 * @param wheres
	 *            set of {@link Where} clauses
	 */
	private static <E> void setQueryWhereParams(TypedQuery<E> query, Set<Where> wheres) {
		for (Iterator<Where> i = wheres.iterator(); i.hasNext();) {
			i.next().compileClause(query);
		}
	}

	/**
	 * Sets the list query fetch range according to limit and offset.
	 * 
	 * @param <E>
	 *            dynamic entity Java Class type
	 * @param query
	 *            JPA query
	 * @param limit
	 *            number of rows to fetch starting from the offset
	 * @param offset
	 *            number of rows to skip before beginning to fetch
	 * @return the same JPA query with the limit and offset values set
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

}