package javax.persistence.filter.core.conditional;

import javax.persistence.TypedQuery;
import javax.persistence.filter.core.Where;

/**
 * @author Michel Risucci
 */
public class IsNotNull extends Where {

	/**
	 * @param path
	 */
	public IsNotNull(String path) {
		super(path);
	}

	@Override
	protected String getJpqlClause() {
		return getRealPath() + " IS NOT NULL ";
	}

	@Override
	public <E> TypedQuery<E> compileClause(TypedQuery<E> query) {
		// Nothing to do here...
		return query;
	}

}