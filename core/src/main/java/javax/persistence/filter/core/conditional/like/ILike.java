package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class ILike extends Like {

	/**
	 * Protected Constructor: needed for inherited classes.
	 * 
	 * @param relativePath
	 * @param percentPosition
	 * @param value
	 */
	protected ILike(String path, PercentPosition percentPosition, String value) {
		super(path, percentPosition, value);
	}

	/**
	 * @param relativePath
	 * @param value
	 */
	public ILike(String path, String value) {
		super(path, PercentPosition.AROUND, value);
	}

	@Override
	protected String getClause() {
		return "UPPER(" + getRealPath() + ") LIKE UPPER(:" + queryParamName + ") ";
	}

	@Override
	protected String generateParamValue() {
		return super.generateParamValue().toUpperCase();
	}

}