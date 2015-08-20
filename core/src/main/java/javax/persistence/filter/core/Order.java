package javax.persistence.filter.core;

/**
 * @author Michel Risucci
 */
public class Order extends VolatilePath {

	/**
	 * @author Michel Risucci
	 */
	public enum Direction {
		ASC, DESC;

		/**
		 * Creates an order clause, using a {@link String} relativePath.
		 * 
		 * @param relativePath
		 * @return
		 */
		public Order createOrder(String path) {
			return new Order(path, this);
		}
	}

	private Direction direction;

	/*
	 * Constructors
	 */

	/**
	 * 
	 */
	private Order(String fullRelativePath, Direction direction) {

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

		this.queryParamName = createQueryParamName();
		this.direction = direction;
	}

	/**
	 * @param fullRelativePath
	 * @return
	 */
	public static Order ascending(String fullRelativePath) {
		return new Order(fullRelativePath, Direction.ASC);
	}

	/**
	 * @param fullRelativePath
	 * @return
	 */
	public static Order descending(String fullRelativePath) {
		return new Order(fullRelativePath, Direction.DESC);
	}

	/*
	 * Implementations
	 */

	@Override
	protected String getClause() {
		return getRealPath() + " " + direction.name() + " ";
	}

	/*
	 * Getters and Setters
	 */

	/**
	 * @return
	 */
	public Direction getDirection() {
		return direction;
	}

}