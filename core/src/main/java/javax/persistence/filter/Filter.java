package javax.persistence.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.filter.core.Order;
import javax.persistence.filter.core.Where;

/**
 * @author Michel Risucci
 * 
 * @param <T>
 */
public class Filter<T> {

	private Class<T> rootType;
	private boolean distinct;
	private List<Where> wheres;
	private List<Order> orders;

	/**
	 * Empty Filter constructor that initializes minimal filter functions.
	 */
	private Filter(Class<T> rootType) {
		super();
		this.rootType = rootType;
		this.distinct = false;
	}

	/**
	 * Filter constructor automatically adds Wheres
	 * 
	 * @param wheres
	 */
	private Filter(Class<T> rootType, Where... wheres) {
		this(rootType, wheres, null);
	}

	/**
	 * Filter constructor automatically adds Orders
	 * 
	 * @param orders
	 */
	private Filter(Class<T> rootType, Order... orders) {
		this(rootType, null, orders);
	}

	/**
	 * Filter constructor automatically adds Wheres and Orders.
	 * 
	 * @param wheres
	 * @param orders
	 */
	private Filter(Class<T> rootType, Where[] wheres, Order[] orders) {
		this(rootType);
		if (wheres != null && wheres.length > 0) {
			add(wheres);
		}
		if (orders != null && orders.length > 0) {
			add(orders);
		}
	}

	/**
	 * Empty Filter constructor that initializes minimal filter functions.
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType) {
		return new Filter<T>(rootType);
	}

	/**
	 * Filter constructor automatically adds Wheres
	 * 
	 * @param wheres
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType, Where... wheres) {
		return new Filter<T>(rootType, wheres);
	}

	/**
	 * Filter constructor automatically adds Orders
	 * 
	 * @param orders
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType, Order... orders) {
		return new Filter<T>(rootType, orders);
	}

	/**
	 * Filter constructor automatically adds Wheres and Orders.
	 * 
	 * @param wheres
	 * @param orders
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType, Where[] wheres,
			Order[] orders) {
		return new Filter<T>(rootType, wheres, orders);
	}

	/**
	 * @return
	 */
	public Class<T> getRootType() {
		return rootType;
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
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * @param distinct
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
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