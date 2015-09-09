package javax.persistence.filter.core.conditional.exact;

/**
 * @author Michel Risucci
 */
public class GreaterThan extends Exact {

	/**
	 * @param path
	 * @param value
	 */
	public GreaterThan(String path, Object value) {
		super(path, Operation.GREATER_THAN, value);
	}

}