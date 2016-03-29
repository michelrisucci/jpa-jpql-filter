package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class ILike extends Like {

	/**
	 * Protected Constructor: needed for inherited classes.
	 * 
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
		super(path, PercentPosition.AROUND, value);
	}

	@Override
	protected String mountClausePart(int index) {
		return "UPPER(" + getRealPath() + ") LIKE UPPER(:" + getQueryParamName(index) + ")";
	}

	@Override
	protected String generateParamValue(int index) {
		return super.generateParamValue(index).toUpperCase();
	}

}