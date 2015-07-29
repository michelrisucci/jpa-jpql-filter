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
			builder.append(relativePath);
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
			return processJoins(aliases, relativePath);
		} else {
			this.realPath = ROOT_PREFIX + SEPARATOR + valueFieldName;
			return "";
		}
	}

	/**
	 * @param aliases
	 * @param last
	 * @return
	 */
	protected String processJoins(Map<String, String> aliases, String last) {

		return null;
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