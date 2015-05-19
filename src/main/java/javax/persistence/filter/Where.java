package javax.persistence.filter;

import java.util.Arrays;

/**
 * @author Michel Risucci
 */
public final class Where {

	private String path;
	private Operator operator;
	private Object[] values;

	/**
	 * @param path
	 * @param operator
	 * @param values
	 */
	private Where(String path, //
			Operator operator, //
			Object... values) {

		this.path = path;
		this.operator = operator;
		this.values = values;
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where equal(String path, Object value) {
		return new Where(path, Operator.EQUAL, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where notEqual(String path, Object value) {
		return new Where(path, Operator.NOT_EQUAL, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where lesserThan(String path, Object value) {
		return new Where(path, Operator.LESSER_THAN, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where lesserThanOrEqual(String path, Object value) {
		return new Where(path, Operator.LESSER_THAN_OR_EQUAL, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where greaterThan(String path, Object value) {
		return new Where(path, Operator.GREATER_THAN, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where greaterThanOrEqual(String path, Object value) {
		return new Where(path, Operator.GREATER_THAN_OR_EQUAL, value);
	}

	/**
	 * @param path
	 * @param initialValue
	 * @param finalValue
	 * @return
	 */
	public static Where between(String path, Object initialValue,
			Object finalValue) {
		return new Where(path, Operator.BETWEEN, initialValue, finalValue);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where like(String path, String value) {
		return new Where(path, Operator.LIKE, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where iLike(String path, String value) {
		return new Where(path, Operator.I_LIKE, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where startsWith(String path, String value) {
		return new Where(path, Operator.STARTS_WITH, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where iStartsWith(String path, String value) {
		return new Where(path, Operator.I_STARTS_WITH, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where endsWith(String path, String value) {
		return new Where(path, Operator.ENDS_WITH, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where iEndsWith(String path, String value) {
		return new Where(path, Operator.I_ENDS_WITH, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where in(String path, Object... values) {
		return new Where(path, Operator.IN, values);
	}

	/**
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * @return
	 */
	public Object[] getValues() {
		return values;
	}

	@Override
	public String toString() {

		if (path != null //
				&& !path.isEmpty() //
				&& values != null //
				&& operator != null) {

			switch (operator) {
			case EQUAL:
				return path + " = " + values[0];
			case NOT_EQUAL:
				return path + " <> " + values[0];
			case LESSER_THAN:
				return path + " < " + values[0];
			case LESSER_THAN_OR_EQUAL:
				return path + " <= " + values[0];
			case GREATER_THAN:
				return path + " > " + values[0];
			case GREATER_THAN_OR_EQUAL:
				return path + " >= " + values[0];
			case BETWEEN:
				return path + " BETWEEN " + values[0] + " AND " + values[1];
			case LIKE:
				return path + " LIKE '%" + values[0] + "%'";
			case I_LIKE:
				return "UPPER(" + path + ") LIKE UPPER('%" + values[0] + "%')";
			case STARTS_WITH:
				return path + " LIKE '" + values[0] + "%'";
			case I_STARTS_WITH:
				return "UPPER(" + path + ") LIKE UPPER('" + values[0] + "%')";
			case ENDS_WITH:
				return path + " LIKE '%" + values[0] + "'";
			case I_ENDS_WITH:
				return "UPPER(" + path + ") LIKE UPPER('%" + values[0] + "')";
			case IN:
				return path + " IN (" + valuesToString() + ") ";
			case NOT_IN:
				return path + " NOT IN (" + valuesToString() + ") ";
			default:
				return " ";
			}
		} else {
			return super.toString();
		}
	}

	/**
	 * @return
	 */
	public String valuesToString() {
		String values = Arrays.deepToString(this.values);
		return values.substring(1, values.length() - 1);
	}

}