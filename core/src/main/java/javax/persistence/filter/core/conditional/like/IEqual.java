package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class IEqual extends ILike {

	/**
	 * @param path
	 * @param value
	 */
	public IEqual(String path, String value) {
		super(path, PercentPosition.NONE, value);
	}

}