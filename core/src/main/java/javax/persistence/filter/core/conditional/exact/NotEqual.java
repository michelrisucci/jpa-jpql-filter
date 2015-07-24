package javax.persistence.filter.core.conditional.exact;

/**
 * @author Michel Risucci
 */
public class NotEqual extends Exact {

	/**
	 * @param relativePath
	 * @param value
	 */
	public NotEqual(String path, Object value) {
		super(path, Operation.NOT_EQUAL, value);
	}

}