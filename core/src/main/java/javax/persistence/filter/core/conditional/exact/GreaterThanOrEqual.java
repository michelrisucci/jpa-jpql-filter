package javax.persistence.filter.core.conditional.exact;

/**
 * @author Michel Risucci
 */
public class GreaterThanOrEqual extends Exact {

	/**
	 * @param relativePath
	 * @param value
	 */
	public GreaterThanOrEqual(String path, Object value) {
		super(path, Operation.GREATER_THAN_OR_EQUAL, value);
	}

}