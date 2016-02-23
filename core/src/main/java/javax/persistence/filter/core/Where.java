package javax.persistence.filter.core;

import javax.persistence.TypedQuery;
import javax.persistence.filter.core.conditional.Between;
import javax.persistence.filter.core.conditional.In;
import javax.persistence.filter.core.conditional.IsNotNull;
import javax.persistence.filter.core.conditional.IsNull;
import javax.persistence.filter.core.conditional.NotIn;
import javax.persistence.filter.core.conditional.exact.Equal;
import javax.persistence.filter.core.conditional.exact.GreaterThan;
import javax.persistence.filter.core.conditional.exact.GreaterThanOrEqual;
import javax.persistence.filter.core.conditional.exact.LesserThan;
import javax.persistence.filter.core.conditional.exact.LesserThanOrEqual;
import javax.persistence.filter.core.conditional.exact.NotEqual;
import javax.persistence.filter.core.conditional.like.EndsWith;
import javax.persistence.filter.core.conditional.like.IEndsWith;
import javax.persistence.filter.core.conditional.like.IEquals;
import javax.persistence.filter.core.conditional.like.ILike;
import javax.persistence.filter.core.conditional.like.IStartsWith;
import javax.persistence.filter.core.conditional.like.Like;
import javax.persistence.filter.core.conditional.like.StartsWith;

/**
 * @author Michel Risucci
 */
public abstract class Where extends VolatilePath {

	protected static final Join DEFAULT_JUNCTION = Join.INNER;

	protected Object[] values;

	/*
	 * Constructors
	 */

	/**
	 * @param fullRelativePath
	 * @param values
	 */
	protected Where(String fullRelativePath, Object... values) {
		this(fullRelativePath, null, values);
	}

	/**
	 * @param fullRelativePath
	 * @param join
	 * @param values
	 */
	protected Where(String fullRelativePath, Join[] joins, Object... values) {

		if (fullRelativePath.contains(".")) {
			int valueDotIndex = fullRelativePath.lastIndexOf('.');

			// Relative path without value field name;
			this.relativePath = fullRelativePath.substring(0, valueDotIndex);
			// Relative path parts according to separator REGEX;
			this.relativePathParts = relativePath.split(SEPARATOR_REGEX);
			// Value field name (only last word);
			this.valueFieldName = fullRelativePath.substring(valueDotIndex + 1);
		} else {
			this.valueFieldName = fullRelativePath;
		}

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
	public static Where between(String path, Object initialValue, Object finalValue) {
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
	public static Where iEquals(String path, String value) {
		return new IEquals(path, value);
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
	 * @param values
	 * @return
	 */
	public static Where in(String path, Object... values) {
		return new In(path, values);
	}

	/**
	 * @param path
	 * @param values
	 * @return
	 */
	public static Where notIn(String path, Object... values) {
		return new NotIn(path, values);
	}

	/**
	 * @param path
	 * @return
	 */
	public static Where isNull(String path) {
		return new IsNull(path);
	}

	/**
	 * @param path
	 * @return
	 */
	public static Where isNotNull(String path) {
		return new IsNotNull(path);
	}

	/*
	 * Implementations
	 */

	/**
	 * @param query
	 */
	public abstract <E> TypedQuery<E> compileClause(TypedQuery<E> query);

	/*
	 * Getters and Setters
	 */

	/**
	 * @return
	 */
	public Object[] getValues() {
		return values;
	}

	/*
	 * To String
	 */

	@Override
	public String toString() {
		return getClause();
	}

}