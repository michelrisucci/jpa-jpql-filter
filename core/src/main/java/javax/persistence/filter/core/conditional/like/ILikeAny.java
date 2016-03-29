package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class ILikeAny extends LikeAny {

	/**
	 * @param path
	 * @param values
	 */
	public ILikeAny(String path, String... values) {
		super(path, values);
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