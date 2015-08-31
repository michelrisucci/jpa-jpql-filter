package javax.persistence.filter.core;

/**
 * Join types for JPQL queries.
 * 
 * @author Michel Risucci
 */
public enum Join {

	INNER("INNER JOIN"), //
	LEFT("LEFT OUTER JOIN"), //
	RIGHT("RIGHT OUTER JOIN");

	private String value;

	/**
	 * @param value
	 */
	private Join(String value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public String getValue() {
		return value;
	}

}