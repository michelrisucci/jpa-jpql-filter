package javax.persistence.filter.core.conditional;

/**
 * @author Michel Risucci
 */
public class NotIn extends In {

	/**
	 * @param path
	 * @param values
	 */
	public NotIn(String path, Object[] values) {
		super(path, values);
	}

	@Override
	protected String getClause(String realPath) {
		return realPath + " NOT IN (:" + varPath + ") ";
	}

}