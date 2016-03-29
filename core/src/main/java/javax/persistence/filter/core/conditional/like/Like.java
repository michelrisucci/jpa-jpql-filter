package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class Like extends LikeAny {

	/**
	 * @param path
	 * @param percentPosition
	 * @param values
	 */
	protected Like(String path, PercentPosition percentPosition, String... values) {
		super(path, percentPosition, values);
	}

	/**
	 * @param path
	 * @param value
	 */
	public Like(String path, String value) {
		this(path, PercentPosition.AROUND, value);
	}

}