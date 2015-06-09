package javax.persistence.filter.core;

import javax.persistence.TypedQuery;
import javax.persistence.filter.core.conditional.Between;
import javax.persistence.filter.core.conditional.In;
import javax.persistence.filter.core.conditional.NotIn;
import javax.persistence.filter.core.conditional.exact.Equal;
import javax.persistence.filter.core.conditional.exact.GreaterThan;
import javax.persistence.filter.core.conditional.exact.GreaterThanOrEqual;
import javax.persistence.filter.core.conditional.exact.LesserThan;
import javax.persistence.filter.core.conditional.exact.LesserThanOrEqual;
import javax.persistence.filter.core.conditional.exact.NotEqual;
import javax.persistence.filter.core.conditional.like.EndsWith;
import javax.persistence.filter.core.conditional.like.IEndsWith;
import javax.persistence.filter.core.conditional.like.ILike;
import javax.persistence.filter.core.conditional.like.IStartsWith;
import javax.persistence.filter.core.conditional.like.Like;
import javax.persistence.filter.core.conditional.like.StartsWith;

/**
 * @author Michel Risucci
 */
public abstract class Where extends VolatilePath {

	protected String varPath;
	protected Object[] values;

	/*
	 * Constructors
	 */

	/**
	 * @param path
	 * @param values
	 */
	protected Where(String path, Object... values) {
		this.path = path;
		this.varPath = createVarPath();
		this.values = values;
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where equal(String path, Object value) {
		return new Equal(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where notEqual(String path, Object value) {
		return new NotEqual(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where lesserThan(String path, Object value) {
		return new LesserThan(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where lesserThanOrEqual(String path, Object value) {
		return new LesserThanOrEqual(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where greaterThan(String path, Object value) {
		return new GreaterThan(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where greaterThanOrEqual(String path, Object value) {
		return new GreaterThanOrEqual(path, value);
	}

	/**
	 * @param path
	 * @param initialValue
	 * @param finalValue
	 * @return
	 */
	public static Where between(String path, Object initialValue,
			Object finalValue) {
		return new Between(path, initialValue, finalValue);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where like(String path, String value) {
		return new Like(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where iLike(String path, String value) {
		return new ILike(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where startsWith(String path, String value) {
		return new StartsWith(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where iStartsWith(String path, String value) {
		return new IStartsWith(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where endsWith(String path, String value) {
		return new EndsWith(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where iEndsWith(String path, String value) {
		return new IEndsWith(path, value);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where in(String path, Object... values) {
		return new In(path, values);
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public static Where notIn(String path, Object... values) {
		return new NotIn(path, values);
	}

	/*
	 * Getters and Setters
	 */

	/**
	 * @return
	 */
	public String getVarPath() {
		return varPath;
	}

	/**
	 * @return
	 */
	public Object[] getValues() {
		return values;
	}

	/*
	 * Implementations
	 */

	/**
	 * @param realPath
	 * @return
	 */
	protected abstract String getClause(String realPath);

	/**
	 * @param query
	 */
	public abstract <E> TypedQuery<E> compileClause(TypedQuery<E> query);

	/*
	 * To String
	 */

	@Override
	public String toString() {
		return toString(path);
	}

	/**
	 * @param realPath
	 * @return
	 */
	public String toString(String realPath) {
		return getClause(realPath);
	}

}