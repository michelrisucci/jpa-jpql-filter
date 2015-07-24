package javax.persistence.filter.core.conditional.exact;

/**
 * @author Michel Risucci
 */
public class LesserThan extends Exact {

	/**
	 * @param relativePath
	 * @param value
	 */
	public LesserThan(String path, Object value) {
		super(path, Operation.LESSER_THAN, value);
	}

}