package javax.persistence.filter.core;

import java.util.Map;
import java.util.regex.Pattern;

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
	protected String createQueryParamName() {
		StringBuilder builder = new StringBuilder("_");
		if (relativePath != null) {
			builder.append(relativePath.replaceAll(SEPARATOR_REGEX, ""));
		}
		builder.append(valueFieldName);
		return builder.toString();
	}

	/**
	 * @param aliases
	 * @return
	 */
	protected String processJoins(Map<String, String> aliases) {
		if (relativePath != null && relativePathParts != null) {
			StringBuilder joins = new StringBuilder();
			// Process join strings;
			return processJoins(aliases, joins, -1, null);
		} else {
			this.realPath = ROOT_PREFIX + SEPARATOR + valueFieldName;
			// No join strings;
			return "";
		}
	}

	/**
	 * @param aliases
	 * @param joins
	 * @param lastIndex
	 * @param last
	 * @return
	 */
	protected String processJoins(Map<String, String> aliases,
			StringBuilder joins, int lastIndex, String last) {

		int currentIndex = lastIndex + 1;
		if (relativePathParts.length > currentIndex) {
			boolean first = lastIndex == -1;
			String prefix = first ? ROOT_PREFIX : last;
			last = prefix + SEPARATOR + relativePathParts[currentIndex];
			String lastAlias = last.replaceAll(SEPARATOR_REGEX, "");

			aliases.put(last, lastAlias);
			joins.append("INNER JOIN ") //
					.append(last) //
					.append(" ") //
					.append(lastAlias) //
					.append(" ");

			return processJoins(aliases, joins, currentIndex, lastAlias);
		} else {
			realPath = last + SEPARATOR + valueFieldName;
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

	/**
	 * Returns the aliased JPQL relativePath.
	 * 
	 * @return
	 */
	protected abstract String getClause();

}