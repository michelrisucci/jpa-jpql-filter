package javax.persistence.filter.core.conditional.like;

import javax.persistence.TypedQuery;
import javax.persistence.filter.core.Where;

/**
 * @author Michel Risucci
 */
public class LikeAny extends Where {

	/**
	 * @author Michel Risucci
	 */
	protected enum PercentPosition {
		NONE, LEFT, RIGHT, AROUND;
	}

	private PercentPosition percentPosition;

	/**
	 * @param path
	 * @param percentPosition
	 * @param values
	 */
	protected LikeAny(String path, PercentPosition percentPosition, String... values) {
		super(path, (Object[]) values);
		this.percentPosition = percentPosition;
	}

	/**
	 * @param path
	 * @param values
	 */
	public LikeAny(String path, String... values) {
		this(path, PercentPosition.AROUND, values);
	}

	@Override
	protected String getJpqlClause() {
		StringBuilder clause = new StringBuilder("( ");

		for (int i = 0; i < values.length; i++) {
			clause.append(mountClausePart(i));
			if (i + 1 < values.length) {
				clause.append(" OR ");
			}
		}
		return clause.append(") ").toString();
	}

	protected String mountClausePart(int index) {
		return getRealPath() + " LIKE :" + getQueryParamName(index);
	}

	@Override
	public <E> TypedQuery<E> compileClause(TypedQuery<E> query) {
		for (int i = 0; i < values.length; i++) {
			query.setParameter(getQueryParamName(i), generateParamValue(i));
		}
		return query;
	}

	/**
	 * @return
	 */
	protected String generateParamValue(int index) {
		// Trimming, removing all spaces and replacing them with "%";
		String value = String.class.cast(values[index]).trim().replaceAll(" +", "%");
		// Adding the LIKE percent '%' operator in the correct position;
		return processPercentPosition(value);
	}

	public String getQueryParamName(int index) {
		return getQueryParamName() + index;
	}

	/**
	 * @param param
	 * @return
	 */
	private String processPercentPosition(String param) {
		if (param != null && !param.isEmpty()) {
			switch (percentPosition) {
			case NONE:
				return param;
			case LEFT:
				return "%" + param;
			case RIGHT:
				return param + "%";
			case AROUND:
				return "%" + param + "%";
			}
		}
		return "%";
	}

}