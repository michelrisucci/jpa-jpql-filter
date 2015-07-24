package javax.persistence.filter.core.conditional.like;

/**
 * @author Michel Risucci
 */
public class StartsWith extends Like {

	/**
	 * @param relativePath
	 * @param value
	 */
	public StartsWith(String path, String value) {
		super(path, PercentPosition.RIGHT, value);
	}

}