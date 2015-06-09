package javax.persistence.filter.core;

/**
 * @author Michel Risucci
 */
public abstract class VolatilePath {

	protected String path;

	/**
	 * 
	 */
	public VolatilePath() {
	}

	/**
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Returns the real aliased JPQL path.
	 * 
	 * @param realPath
	 * @return
	 */
	protected abstract String getClause(String realPath);

	/**
	 * Creates a path string to be used on JPQL parameters.
	 * 
	 * @return
	 */
	protected String createVarPath() {
		return "_" + path.replaceAll("\\.", "");
	}

}