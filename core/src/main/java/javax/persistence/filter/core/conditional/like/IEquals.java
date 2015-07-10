package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class IEquals extends ILike {

	/**
	 * @param path
	 * @param value
	 */
	public IEquals(String path, String value) {
		super(path, PercentPosition.NONE, value);
	}

}