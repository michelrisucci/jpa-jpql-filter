package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class IStartsWith extends ILike {

	/**
	 * @param path
	 * @param value
	 */
	public IStartsWith(String path, String value) {
		super(path, PercentPosition.RIGHT, value);
	}

}