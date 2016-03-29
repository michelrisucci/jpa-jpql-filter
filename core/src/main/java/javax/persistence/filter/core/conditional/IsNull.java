package javax.persistence.filter.core.conditional;

import javax.persistence.TypedQuery;
import javax.persistence.filter.core.Where;

/**
 * @author Michel Risucci
 */
public class IsNull extends Where {

	/**
	 * @param path
	 */
	public IsNull(String path) {
		super(path);
	}

	protected String getJpqlClause() {
		return getRealPath() + " IS NULL ";
	}

	@Override
	public <E> TypedQuery<E> compileClause(TypedQuery<E> query) {
		// Nothing to do here...
		return query;
	}

}