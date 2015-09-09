package javax.persistence.filter.core.conditional.exact;

/**
 * @author Michel Risucci
 */
public class LesserThanOrEqual extends Exact {

	/**
	 * @param path
	 * @param value
	 */
	public LesserThanOrEqual(String path, Object value) {
		super(path, Operation.LESSER_THAN_OR_EQUAL, value);
	}

}