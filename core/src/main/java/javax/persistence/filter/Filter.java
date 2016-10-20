package javax.persistence.filter;

import java.util.Collection;
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
 *            dynamic entity Java type
 */
public class Filter<T> {

	private static final Map<Class<?>, String> ENTITY_NAME_CACHE_MAP = new HashMap<Class<?>, String>();

	private Class<T> rootType;
	private String entityName;
	private boolean distinct;
	private Set<Where> wheres;
	private Set<Order> orders;
	private int pathIncrementor = 0;

	/**
	 * Empty Filter constructor that initializes minimal filter functions.
	 */
	private Filter(Class<T> rootType, boolean distinct) {
		super();
		this.rootType = rootType;
		this.distinct = distinct;
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
		this(rootType, false);
		if (wheres != null && wheres.length > 0) {
			add(wheres);
		}
		if (orders != null && orders.length > 0) {
			add(orders);
		}
	}

	/**
	 * Empty Filter constructor that initializes minimal filter functions.
	 * 
	 * @param <T>
	 *            dynamic entity Java type
	 * @param rootType
	 *            root entity Java Class type
	 * @return an empty {@link Filter} definition
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType) {
		return new Filter<T>(rootType, false);
	}

	/**
	 * Empty Filter constructor that initializes minimal filter functions.
	 * 
	 * @param <T>
	 *            dynamic entity Java type
	 * @param rootType
	 *            root entity Java Class type
	 * @param distinct
	 *            if is to add "DISTINCT" to selection
	 * @return an empty {@link Filter} definition
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType, boolean distinct) {
		return new Filter<T>(rootType, distinct);
	}

	/**
	 * Filter constructor automatically adds Wheres
	 * 
	 * @param <T>
	 *            dynamic entity Java type
	 * @param rootType
	 *            root entity Java Class type
	 * @param wheres
	 *            optional array of {@link Where} conditionals
	 * @return a {@link Filter} definition with the defined {@link Where}
	 *         conditionals
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType, Where... wheres) {
		return new Filter<T>(rootType, wheres);
	}

	/**
	 * Filter constructor automatically adds Orders
	 * 
	 * @param <T>
	 *            dynamic entity Java type
	 * @param rootType
	 *            root entity Java Class type
	 * @param orders
	 *            optional array of {@link Order} conditionals
	 * @return a {@link Filter} definition with the defined {@link Order}
	 *         conditionals
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType, Order... orders) {
		return new Filter<T>(rootType, orders);
	}

	/**
	 * Filter constructor automatically adds Wheres and Orders.
	 * 
	 * @param <T>
	 *            dynamic entity Java type
	 * @param rootType
	 *            root entity Java Class type
	 * @param wheres
	 *            array of {@link Where} conditionals
	 * @param orders
	 *            array of {@link Order} conditionals
	 * @return a {@link Filter} definition with the defined {@link Where} and
	 *         {@link Order} conditionals
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType, Where[] wheres, Order[] orders) {
		return new Filter<T>(rootType, wheres, orders);
	}

	/**
	 * Filter constructor automatically adds Wheres and Orders.
	 * 
	 * @param <T>
	 *            dynamic entity Java type
	 * @param rootType
	 *            root entity Java Class type
	 * @param wheres
	 *            any collection of {@link Where} conditionals
	 * @param orders
	 *            any collection of {@link Order} conditionals
	 * @return a {@link Filter} definition with the defined {@link Where} and
	 *         {@link Order} conditionals
	 */
	public static <T> Filter<T> newInstance(Class<T> rootType, Collection<Where> wheres, Collection<Order> orders) {
		Where[] wheresArray = wheres.toArray(new Where[wheres.size()]);
		Order[] ordersArray = orders.toArray(new Order[orders.size()]);

		return new Filter<T>(rootType, wheresArray, ordersArray);
	}

	/**
	 * @return root entity Java Class type
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
				entityName = rootType.getSimpleName();
				Entity entity = rootType.getAnnotation(Entity.class);
				if (entity != null && !entity.name().isEmpty()) {
					entityName = entity.name();
				}
				ENTITY_NAME_CACHE_MAP.put(rootType, entityName);
			}
		}
		return entityName;
	}

	/**
	 * @return if this {@link Filter} model processes distinction (adds DISTINCT
	 *         after "SELECT" clause)
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * @param distinct
	 *            sets if this {@link Filter} model processes distinction (adds
	 *            DISTINCT after "SELECT" clause)
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * @return {@link Where} conditionals
	 */
	public Set<Where> getWheres() {
		return wheres;
	}

	/**
	 * @return {@link Order} conditionals
	 */
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * @return
	 */
	public int incrementPathSuffix() {
		return pathIncrementor++;
	}

	/**
	 * Adds {@link Where} conditionals to this {@link Filter} model
	 * 
	 * @param wheres
	 *            {@link Where} conditionals
	 * @return this {@link Filter}
	 */
	public Filter<T> add(Where... wheres) {
		if (this.wheres == null) {
			this.wheres = new LinkedHashSet<Where>();
		}
		for (Where where : wheres) {
			this.wheres.add(where);
			where.postProcess(this);
		}
		return this;
	}

	/**
	 * Adds {@link Order} conditionals to this {@link Filter} model
	 * 
	 * @param orders
	 *            {@link Order} conditionals
	 * @return this {@link Filter}
	 */
	public Filter<T> add(Order... orders) {
		if (this.orders == null) {
			this.orders = new LinkedHashSet<Order>();
		}
		for (Order order : orders) {
			this.orders.add(order);
			order.postProcess(this);
		}
		return this;
	}

	/**
	 * Adds {@link Where} conditionals to this {@link Filter} model
	 * 
	 * @param wheres
	 *            {@link Where} conditionals
	 * @param orders
	 *            {@link Order} conditionals
	 * @return this {@link Filter}
	 */
	public Filter<T> add(Collection<Where> wheres, Collection<Order> orders) {
		if (wheres != null && !wheres.isEmpty()) {
			add(wheres.toArray(new Where[wheres.size()]));
		}
		if (orders != null && !orders.isEmpty()) {
			add(orders.toArray(new Order[orders.size()]));
		}
		return this;
	}

}