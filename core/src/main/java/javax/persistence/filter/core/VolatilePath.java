package javax.persistence.filter.core;

import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.filter.Filter;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Michel Risucci
 */
public abstract class VolatilePath {

	protected static final String ROOT_PREFIX = "x";
	protected static final String SEPARATOR = ".";
	protected static final String SEPARATOR_REGEX = Pattern.quote(SEPARATOR);

	protected String relativePath;
	protected String[] relativePathParts;
	protected String valueFieldName;
	protected String queryParamName;

	// Must be PRIVATE to prevent direct access: use getter instead.
	private String realPath;

	/**
	 * 
	 */
	public VolatilePath() {
	}

	/**
	 * Creates a relativePath string to be used on JPQL parameters.
	 * 
	 * @return
	 */
	protected String createQueryParamName(Filter<?> filter) {
		StringBuilder builder = new StringBuilder("_");
		if (relativePath != null) {
			builder.append(relativePath.replaceAll(SEPARATOR_REGEX, ""));
		}
		builder.append(valueFieldName);
		builder.append(filter.incrementPathSuffix());
		return builder.toString();
	}

	/**
	 * @param filter
	 */
	public void postProcess(Filter<?> filter) {
		this.queryParamName = createQueryParamName(filter);
	}

	/**
	 * @param aliases
	 * @return
	 */
	protected String processAliases(Set<String> aliases) {
		StringBuilder processed = new StringBuilder();
		if (relativePath != null && relativePathParts != null) {
			String prefix = ROOT_PREFIX + StringUtils.join(relativePathParts);
			this.setRealPath(prefix + SEPARATOR + valueFieldName);
			if (!aliases.contains(prefix)) {
				StringBuilder joins = new StringBuilder();
				processed.append(processJoins(aliases, joins, -1, null));
			}
		} else {
			this.setRealPath(ROOT_PREFIX + SEPARATOR + valueFieldName);
		}
		return processed.toString();
	}

	/**
	 * @param aliases
	 * @param joins
	 * @param lastIndex
	 * @param last
	 * @return
	 */
	protected String processJoins(Set<String> aliases, StringBuilder joins, int lastIndex, String last) {
		int currentIndex = lastIndex + 1;
		if (relativePathParts.length > currentIndex) {
			boolean first = lastIndex == -1;
			String prefix = first ? ROOT_PREFIX : last;
			last = prefix + SEPARATOR + relativePathParts[currentIndex];
			String lastAlias = last.replaceAll(SEPARATOR_REGEX, "");

			aliases.add(last);
			aliases.add(lastAlias);

			// TODO enable users to change join type.
			joins.append(Join.INNER.getValue()) //
					.append(" ") //
					.append(last) //
					.append(" ") //
					.append(lastAlias) //
					.append(" ");

			return processJoins(aliases, joins, currentIndex, lastAlias);
		} else {
			this.setRealPath(last + SEPARATOR + valueFieldName);
			return joins.toString();
		}
	}

	/*
	 * Getters and Setters
	 */

	/**
	 * @return
	 */
	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * @return
	 */
	public String[] getRelativePathParts() {
		return relativePathParts;
	}

	/**
	 * @return
	 */
	public String getValueFieldName() {
		return valueFieldName;
	}

	/**
	 * @return
	 */
	public String getQueryParamName() {
		return queryParamName;
	}

	/**
	 * @return
	 */
	public String getRealPath() {
		return realPath == null ? relativePath : realPath;
	}

	/**
	 * @param realPath
	 */
	protected void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	/*
	 * Equals and Hashcode
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((queryParamName == null) ? 0 : queryParamName.hashCode());
		result = prime * result + ((relativePath == null) ? 0 : relativePath.hashCode());
		result = prime * result + ((valueFieldName == null) ? 0 : valueFieldName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VolatilePath other = (VolatilePath) obj;
		if (queryParamName == null) {
			if (other.queryParamName != null)
				return false;
		} else if (!queryParamName.equals(other.queryParamName))
			return false;
		if (relativePath == null) {
			if (other.relativePath != null)
				return false;
		} else if (!relativePath.equals(other.relativePath))
			return false;
		if (valueFieldName == null) {
			if (other.valueFieldName != null)
				return false;
		} else if (!valueFieldName.equals(other.valueFieldName))
			return false;
		return true;
	}

	/**
	 * Returns the aliased JPQL relativePath.
	 * 
	 * @return
	 */
	protected abstract String getJpqlClause();

}