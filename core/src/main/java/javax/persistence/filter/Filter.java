package javax.persistence.filter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
	private List<Where> wheres;
	private List<Order> orders;

	/**
	 * Empty Filter constructor that initializes minimal filter functions.
	 */
	public Filter() {
		super();
		this.rootType = getRecursiveType();
	}

	/**
	 * Filter constructor automatically adds Wheres and Orders.
	 * 
	 * @param wheres
	 * @param orders
	 */
	public Filter(Where[] wheres, Order... orders) {
		this();
		if (wheres.length > 0) {
			add(wheres);
		}
		if (orders.length > 0) {
			add(orders);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<T> getRecursiveType() {
		Type genericType = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = ParameterizedType.class.cast(genericType);
		return Class.class.cast(paramType.getActualTypeArguments()[0]);
	}

	/**
	 * @param rootType
	 * @return
	 */
	public static <T> Filter<T> newInstance() {
		return new Filter<T>();
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