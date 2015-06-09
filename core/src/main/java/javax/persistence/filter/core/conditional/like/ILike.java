package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class ILike extends Like {

	/**
	 * @param path
	 * @param percentPosition
	 * @param value
	 */
	protected ILike(String path, PercentPosition percentPosition, String value) {
		super(path, percentPosition, value);
	}

	/**
	 * @param path
	 * @param value
	 */
	public ILike(String path, String value) {
		this(path, PercentPosition.AROUND, value);
	}

	@Override
	protected String getClause(String realPath) {
		return "UPPER(" + realPath + ") LIKE UPPER(:" + varPath + ") ";
	}

	@Override
	protected String generateParamValue() {
		return super.generateParamValue().toUpperCase();
	}

}