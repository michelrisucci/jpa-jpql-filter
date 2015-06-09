package javax.persistence.filter.core.conditional.exact;

/**
 * @author Michel Risucci
 */
public class Equal extends Exact {

	/**
	 * @param path
	 * @param value
	 */
	public Equal(String path, Object value) {
		super(path, Operation.EQUAL, value);
	}

}