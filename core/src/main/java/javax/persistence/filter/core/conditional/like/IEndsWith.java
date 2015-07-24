package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class IEndsWith extends ILike {

	/**
	 * @param relativePath
	 * @param value
	 */
	public IEndsWith(String path, String value) {
		super(path, PercentPosition.LEFT, value);
	}

}