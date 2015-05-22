package javax.persistence.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.filter.exception.FirstResultOutOfRangeException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Michel Risucci
 */
public class FilterServices {

	private static final Log log = LogFactory.getLog(FilterServices.class);
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

		if ((count == 0 && firstResult != 0) || (count > 0 && firstResult > count)) {
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

		StringBuilder builder = new StringBuilder() //
				.append("SELECT COUNT(x) ") //
				.append("FROM " + entityName + " x ");

		Map<String, String> pathVars = null;
		List<Where> wheres = filter.getWheres();
		if (wheres != null && !wheres.isEmpty()) {
			pathVars = buildJpqlWhereParams(builder, wheres);
		}

		String jpql = builder.toString();
		log.info(jpql);

		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);

		if (wheres != null && !wheres.isEmpty()) {
			setQueryWhereParams(query, wheres, pathVars);
		}

		return query.getSingleResult();
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

		StringBuilder builder = new StringBuilder() //
				.append("SELECT x ") //
				.append("FROM " + entityName + " x ");

		Map<String, String> pathVars = null;
		List<Where> wheres = filter.getWheres();
		if (wheres != null && !wheres.isEmpty()) {
			pathVars = buildJpqlWhereParams(builder, wheres);
		}

		List<Order> orders = filter.getOrders();
		if (orders != null && !orders.isEmpty()) {
			setJpqlOrderParams(builder, orders);
		}

		String jpql = builder.toString();
		log.info(jpql);

		TypedQuery<E> query = entityManager.createQuery(jpql,
				filter.getRootType());

		if (wheres != null && !wheres.isEmpty()) {
			setQueryWhereParams(query, wheres, pathVars);
		}

		return query //
				.setFirstResult(firstResult) //
				.setMaxResults(maxResults) //
				.getResultList();
	}

	/**
	 * @param builder
	 * @param wheres
	 * @return
	 */
	private static Map<String, String> buildJpqlWhereParams(
			StringBuilder builder, List<Where> wheres) {
		Map<String, String> pathVars = new HashMap<String, String>();
		builder.append("WHERE ");
		for (Iterator<Where> i = wheres.listIterator(); i.hasNext();) {
			Where where = i.next();
			String pathVar = pathToVar(where.getPath());
			pathVars.put(where.getPath(), pathVar);

			builder.append(compileJpqlWhereClauses( //
					where.getOperator(), //
					where.getPath(), //
					pathVar));

			if (i.hasNext()) {
				builder.append("AND");
			}
			builder.append(" ");
		}
		return pathVars;
	}

	/**
	 * @param builder
	 * @param orders
	 */
	private static void setJpqlOrderParams(StringBuilder builder,
			List<Order> orders) {
		builder.append("ORDER BY ");
		for (Iterator<Order> i = orders.listIterator(); i.hasNext();) {
			Order order = i.next();

			builder.append(order.getPath() + " " + order.getDirection().name());

			if (i.hasNext()) {
				builder.append(",");
			}
			builder.append(" ");
		}
	}

	/**
	 * @param path
	 * @return
	 */
	private static String pathToVar(String path) {
		return "_" + path.replaceAll("\\.", "");
	}

	/**
	 * @param operator
	 * @param path
	 * @param varPath
	 * @param values
	 * @return
	 */
	private static String compileJpqlWhereClauses(Operator operator, //
			String path, //
			String varPath) {

		switch (operator) {
		case EQUAL:
			return "x." + path + " = :" + varPath + " ";
		case NOT_EQUAL:
			return "x." + path + " <> :" + varPath + " ";
		case LESSER_THAN:
			return "x." + path + " < :" + varPath + " ";
		case LESSER_THAN_OR_EQUAL:
			return "x." + path + " <= :" + varPath + " ";
		case GREATER_THAN:
			return "x." + path + " > :" + varPath + " ";
		case GREATER_THAN_OR_EQUAL:
			return "x." + path + " >= :" + varPath + " ";
		case BETWEEN:
			return "x." + path + " BETWEEN :" + //
					varPath + "Lesser AND :" + //
					varPath + "Greater ";
		case LIKE:
		case STARTS_WITH:
		case ENDS_WITH:
			return "x." + path + " LIKE :" + //
					varPath + " ";
		case I_LIKE:
		case I_STARTS_WITH:
		case I_ENDS_WITH:
			return "UPPER(x." + path + ") LIKE UPPER(:" //
					+ varPath + ") ";
		case IN:
			return "x." + path + " IN (:" + varPath + ") ";
		case NOT_IN:
			return "x." + path + " NOT IN (:" + varPath + ") ";
		default:
			return " ";
		}
	}

	/**
	 * @param builder
	 * @param wheres
	 * @param pathVars
	 */
	private static <E> void setQueryWhereParams(TypedQuery<E> query,
			List<Where> wheres, Map<String, String> pathVars) {
		for (Where where : wheres) {
			String pathVar = pathToVar(where.getPath());
			compileQueryWhereClauses(query, //
					where.getOperator(), //
					pathVar, //
					where.getValues());
		}
	}

	/**
	 * @param query
	 * @param operator
	 * @param varPath
	 * @param values
	 */
	private static <E> TypedQuery<E> compileQueryWhereClauses(
			TypedQuery<E> query, Operator operator, //
			String varPath, Object... values) {

		switch (operator) {
		case EQUAL:
		case NOT_EQUAL:
		case LESSER_THAN:
		case LESSER_THAN_OR_EQUAL:
		case GREATER_THAN:
		case GREATER_THAN_OR_EQUAL:
			return query.setParameter(varPath, values[0]);
		case BETWEEN:
			return query.setParameter(varPath + "Lesser", values[0])
					.setParameter(varPath + "Greater", values[1]);
		case LIKE:
		case I_LIKE:
			return query.setParameter(varPath, "%" + values[0] + "%");
		case STARTS_WITH:
		case I_STARTS_WITH:
			return query.setParameter(varPath, values[0] + "%");
		case ENDS_WITH:
		case I_ENDS_WITH:
			return query.setParameter(varPath, "%" + values[0]);
		case IN:
		case NOT_IN:
			return query.setParameter(varPath, values);
		default:
			return query;
		}
	}

}