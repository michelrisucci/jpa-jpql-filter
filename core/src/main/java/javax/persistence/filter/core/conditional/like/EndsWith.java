package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class EndsWith extends Like {

	/**
	 * @param relativePath
	 * @param value
	 */
	public EndsWith(String path, String value) {
		super(path, PercentPosition.LEFT, value);
	}

}