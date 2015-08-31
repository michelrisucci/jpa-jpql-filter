package javax.persistence.filter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.filter.core.Order;
import javax.persistence.filter.core.Where;

/**
 * Filter model that holds query parameters.
 * 
 * @author Michel Risucci
 * 
 * @param <T>
 */
public class Filter<T> {

	private static final Map<Class<?>, String> ENTITY_NAME_CACHE_MAP = new HashMap<Class<?>, String>();

	private Class<T> rootType;
	private String entityName;
	private boolean distinct;
	private Set<Where> wheres;
	private Set<Order> orders;

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
	public static <T> Filter<T> newInstance(Class<T> rootType, Where[] wheres, Order[] orders) {
		return new Filter<T>(rootType, wheres, orders);
	}

	/**
	 * @return
	 */
	public Class<T> getRootType() {
		return rootType;
	}

	/**
	 * Process entity type and returns its JPA name. By performance issues, it
	 * checks if an internal cache already contains the entity name.
	 * 
	 * @return JPA entity name
	 */
	public String getEntityName() {

		if (entityName == null) {
			// Checking internal cache for entity name.
			entityName = ENTITY_NAME_CACHE_MAP.get(rootType);

			// Otherwise, process entity and fill internal cache.
			if (entityName == null) {
				Entity entity = rootType.getAnnotation(Entity.class);
				if (entity != null && !entity.name().isEmpty()) {
					entityName = entity.name();
				} else {
					entityName = rootType.getSimpleName();
				}
				ENTITY_NAME_CACHE_MAP.put(rootType, entityName);
			}
		}
		return entityName;
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
	public Set<Where> getWheres() {
		return wheres;
	}

	/**
	 * @return
	 */
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * @param wheres
	 * @return
	 */
	public Filter<T> add(Where... wheres) {
		if (this.wheres == null) {
			this.wheres = new LinkedHashSet<Where>();
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
			this.orders = new LinkedHashSet<Order>();
		}
		this.orders.addAll(Arrays.asList(orders));
		return this;
	}

}