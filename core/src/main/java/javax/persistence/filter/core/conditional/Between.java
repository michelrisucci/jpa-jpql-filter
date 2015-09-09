package javax.persistence.filter.core.conditional;

import javax.persistence.TypedQuery;
import javax.persistence.filter.core.Where;

/**
 * @author Michel Risucci
 */
public class Between extends Where {

	private static final String LESSER_SUFFIX = "_LESSER";
	private static final String GREATER_SUFFIX = "_GREATER";

	/**
	 * @param path
	 * @param initialValue
	 * @param finalValue
	 */
	public Between(String path, Object initialValue, Object finalValue) {
		super(path, initialValue, finalValue);
	}

	@Override
	protected String getClause() {
		return getRealPath() //
				+ " BETWEEN " //
				+ ":" + getLesserVarPath() //
				+ " AND " //
				+ ":" + getGreaterVarPath() //
				+ " ";
	}

	@Override
	public <E> TypedQuery<E> compileClause(TypedQuery<E> query) {
		return query.setParameter(getLesserVarPath(), values[0]) //
				.setParameter(getGreaterVarPath(), values[1]);
	}

	/**
	 * @return
	 */
	private String getLesserVarPath() {
		return queryParamName + LESSER_SUFFIX;
	}

	/**
	 * @return
	 */
	private String getGreaterVarPath() {
		return queryParamName + GREATER_SUFFIX;
	}

}