package javax.persistence.filter.core;

/**
 * @author Michel Risucci
 */
public abstract class VolatilePath {

	protected String path;

	private String realPath;

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
	 * @return
	 */
	public String getRealPath() {
		return realPath == null ? path : realPath;
	}

	/**
	 * @param realPath
	 */
	protected void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	/**
	 * Returns the aliased JPQL path.
	 * 
	 * @return
	 */
	protected abstract String getClause();

	/**
	 * Creates a path string to be used on JPQL parameters.
	 * 
	 * @return
	 */
	protected String createVarPath() {
		return "_" + path.replaceAll("\\.", "");
	}

}