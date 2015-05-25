package javax.persistence.filter;

/**
 * @author Michel Risucci
 */
public class Order {

	/**
	 * @author Michel Risucci
	 */
	public enum Direction {
		ASC, DESC;

		/**
		 * Creates an order clause, using a {@link String} path.
		 * 
		 * @param path
		 * @return
		 */
		public Order createOrder(String path) {
			return new Order(path, this);
		}
	}

	private String path;
	private Direction direction;

	/**
	 * 
	 */
	private Order(String path, Direction direction) {
		this.path = path;
		this.direction = direction;
	}

	/**
	 * @param path
	 * @return
	 */
	public static Order ascending(String path) {
		return new Order(path, Direction.ASC);
	}

	/**
	 * @param path
	 * @return
	 */
	public static Order descending(String path) {
		return new Order(path, Direction.DESC);
	}

	/**
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}