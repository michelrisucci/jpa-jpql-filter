package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class NotLike extends Like {

	/**
	 * @param path
	 * @param percentPosition
	 * @param values
	 */
	protected NotLike(String path, PercentPosition percentPosition, String... values) {
		super(path, percentPosition, values);
	}

	/**
	 * @param path
	 * @param value
	 */
	public NotLike(String path, String value) {
		this(path, PercentPosition.AROUND, value);
	}

	@Override
	protected String mountClausePart(int index) {
		return getRealPath() + " NOT LIKE :" + getQueryParamName(index);
	}

}