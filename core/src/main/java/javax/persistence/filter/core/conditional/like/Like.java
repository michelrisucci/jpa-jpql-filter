package javax.persistence.filter.core.conditional.like;

import javax.persistence.TypedQuery;
import javax.persistence.filter.core.Where;

/**
 * @author Michel Risucci
 */
public class Like extends Where {

	private PercentPosition percentPosition;

	/**
	 * @param path
	 * @param percentPosition
	 * @param value
	 */
	protected Like(String path, PercentPosition percentPosition, String value) {
		super(path, value);
		this.percentPosition = percentPosition;
	}

	/**
	 * @param path
	 * @param value
	 */
	public Like(String path, String value) {
		this(path, PercentPosition.AROUND, value);
	}

	/**
	 * @return
	 */
	public PercentPosition getPercentPosition() {
		return percentPosition;
	}

	@Override
	protected String getClause(String realPath) {
		return realPath + " LIKE :" + varPath + " ";
	}

	@Override
	public <E> TypedQuery<E> compileClause(TypedQuery<E> query) {
		String value = generateParamValue();
		return query.setParameter(varPath, value);
	}

	/**
	 * @param value
	 * @return
	 */
	protected String generateParamValue() {
		String value = (String) values[0];
		if (value != null && !value.isEmpty()) {
			value = value //
					// Trimming;
					.trim() //
					// Removing multiple spaces between words;
					.replaceAll(" +", " ") //
					// Replacing all remaining single spaces to '%';
					.replaceAll(" ", "%");
		}
		// Adding the LIKE percent '%' operator in the correct position;
		return processPercentPosition(value);
	}

	/**
	 * @param param
	 * @return
	 */
	private String processPercentPosition(String param) {
		if (param != null && !param.isEmpty()) {
			switch (percentPosition) {
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

	/**
	 * @author Michel Risucci
	 */
	protected enum PercentPosition {
		LEFT, RIGHT, AROUND;
	}

}