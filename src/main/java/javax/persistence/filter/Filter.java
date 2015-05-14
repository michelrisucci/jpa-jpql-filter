package javax.persistence.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Michel Risucci
 * 
 * @param <T>
 */
public class Filter<T> {

	private Class<T> rootType;
	private List<Where> wheres;
	private List<Order> orders;

	/**
	 * Filter constructor that uses default first and max results.
	 */
	private Filter(Class<T> rootType) {
		super();
		this.rootType = rootType;
	}

	/**
	 * @param rootType
	 * @return
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType) {
		return new Filter<T>(rootType);
	}

	/**
	 * @return
	 */
	public Class<T> getRootType() {
		return rootType;
	}

	/**
	 * @param rootType
	 */
	public void setRootType(Class<T> rootType) {
		this.rootType = rootType;
	}

	/**
	 * @param wheres
	 * @return
	 */
	public Filter<T> add(Where... wheres) {
		if (this.wheres == null) {
			this.wheres = new ArrayList<Where>();
		}
		this.wheres.addAll(Arrays.asList(wheres));
		return this;
	}

	/**
	 * @param orders
	 * @return
	 */
	public Filter<T> add(Order... orders) {
		if (this.orders == null) {
			this.orders = new ArrayList<Order>();
		}
		this.orders.addAll(Arrays.asList(orders));
		return this;
	}

	/**
	 * @return
	 */
	public List<Where> getWheres() {
		return wheres;
	}

	/**
	 * @return
	 */
	public List<Order> getOrders() {
		return orders;
	}

}