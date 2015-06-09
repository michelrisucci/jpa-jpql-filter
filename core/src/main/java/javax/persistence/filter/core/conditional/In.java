package javax.persistence.filter.core.conditional;

import java.util.Arrays;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.filter.core.Where;

/**
 * @author Michel Risucci
 */
public class In extends Where {

	/**
	 * @param path
	 * @param values
	 */
	public In(String path, Object[] values) {
		super(path, values);
	}

	@Override
	protected String getClause(String realPath) {
		return realPath + " IN (:" + varPath + ") ";
	}

	@Override
	public <E> TypedQuery<E> compileClause(TypedQuery<E> query) {
		List<Object> valuesAsList = Arrays.asList(values);
		return query.setParameter(varPath, valuesAsList);
	}

}