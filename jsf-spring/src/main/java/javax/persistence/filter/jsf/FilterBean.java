package javax.persistence.filter.jsf;

import static javax.persistence.filter.core.Order.Direction.ASC;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.core.Order.Direction;
import javax.persistence.filter.exception.OffsetOutOfRangeException;
import javax.persistence.filter.service.FilterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Utility JSF Managed Bean with a complete JPA JPQL Filter infrastructure.
 * 
 * @author Michel Risucci
 *
 * @param <E>
 * @param <F>
 */
public abstract class FilterBean<E, S extends FilterService<E, ?, ?>> extends SpringBeanAutowiringSupport {

	private static final int DEFAULT_PAGE_SIZE = 20;

	@Autowired
	private S filterService;
	// Filter Conditional Params
	private Map<String, Object> filterMap;
	// Filter Ordinal Params
	private LinkedHashMap<String, Direction> orderMap;

	// Pagination
	private int currentPage;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private PageFilter<E> pageFilter;

	@PostConstruct
	protected void postConstruct() {
		eraseAndFilter();
	}

	/**
	 * 
	 */
	public void eraseAndFilter() {
		this.pageFilter = eraseAndFilter(this.currentPage, this.pageSize);
	}

	/**
	 * 
	 */
	public PageFilter<E> eraseAndFilter(int currentPage, int pageSize) {
		Filter<E> filter = eraseFilter();
		return doFilter(filter, currentPage, pageSize, filterMap, orderMap);
	}

	/**
	 * 
	 */
	public void doFilter() {
		this.pageFilter = doFilter(this.currentPage, this.pageSize);
	}

	/**
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageFilter<E> doFilter(int currentPage, int pageSize) {
		Filter<E> filter = newFilter();
		return doFilter(filter, currentPage, pageSize, filterMap, orderMap);
	}

	/**
	 * Low-level filter method that processes filter map.
	 * 
	 * @param filter
	 * @param page
	 * @param pageSize
	 * @param filterMap
	 * @param orderMap
	 * @return
	 */
	protected PageFilter<E> doFilter(Filter<E> filter, int page, int pageSize, Map<String, Object> filterMap,
			LinkedHashMap<String, Direction> orderMap) {

		// Adding filter clauses;
		this.addWheres(filter, filterMap);

		// Adding filter orders
		Iterator<Entry<String, Direction>> oIter = orderMap.entrySet().iterator();
		this.addOrders(filter, oIter);

		// Filtering
		return doFilter(filter, page, pageSize);
	}

	/**
	 * Low-level filter method.
	 * 
	 * @param filter
	 * @param page
	 * @param pageSize
	 * @return
	 */
	protected PageFilter<E> doFilter(Filter<E> filter, int page, int pageSize) {
		try {
			int initial = (page - 1) * pageSize;
			initial = initial >= 0 ? initial : 0;
			return filterService.filter(filter, initial, pageSize);
		} catch (OffsetOutOfRangeException e) {
			this.currentPage = 1;
			return doFilter(filter, currentPage, pageSize);
		}
	}

	/*
	 * Abstract Methods
	 */

	/**
	 * Adds filter conditional clauses;
	 */
	protected abstract void addWheres(Filter<E> filter, Map<String, Object> map);

	/**
	 * Adds filter ordering clauses;
	 */
	protected void addOrders(Filter<E> filter, Iterator<Entry<String, Direction>> iterator) {
		while (iterator.hasNext()) {
			Entry<String, Direction> entry = iterator.next();
			Direction direction = entry.getValue();
			String path = entry.getKey();
			filter.add(direction.createOrder(path));
		}
	}

	/**
	 * @param relativePath
	 */
	public void changeOrderAndFilter(String path) {
		changeOrder(path);
		doFilter();
	}

	/**
	 * @param relativePath
	 */
	public void changeOrder(String path) {
		changeOrder(path, null);
	}

	/**
	 * @param relativePath
	 * @param direction
	 */
	public void changeOrder(String path, Direction direction) {
		if (direction == null) {
			// Getting last order direction.
			Direction last = orderMap.get(path);
			orderMap.put(path, last == null ? ASC : last.invert());
		} else {
			orderMap.put(path, direction);
		}
	}

	/**
	 * @param relativePath
	 * @return
	 */
	public int getOrderSequence(String path) {
		Iterator<Entry<String, Direction>> i = orderMap.entrySet().iterator();
		int count = 1;
		while (i.hasNext()) {
			Entry<String, Direction> entry = i.next();
			if (entry.getKey().equals(path)) {
				return count;
			}
			count++;
		}
		return -1;
	}

	/*
	 * Utilities
	 */

	/**
	 * @return
	 */
	protected Filter<E> eraseFilter() {
		return eraseFilter(null);
	}

	/**
	 * @param pageSize
	 * @return
	 */
	protected Filter<E> eraseFilter(Integer pageSize) {
		this.filterMap = new HashMap<String, Object>();
		this.orderMap = new LinkedHashMap<String, Direction>();
		this.currentPage = 1;
		if (pageSize != null) {
			this.pageSize = pageSize;
		}

		return newFilter();
	}

	@SuppressWarnings("unchecked")
	protected Filter<E> newFilter() {
		Type superType = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = ParameterizedType.class.cast(superType);
		Class<E> entityType = Class.class.cast(paramType.getActualTypeArguments()[0]);
		return Filter.newInstance(entityType);
	}

	/*
	 * Getters and Setters
	 */

	/**
	 * @return
	 */
	public S getFilterService() {
		return filterService;
	}

	/**
	 * @param filterService
	 */
	public void setFilterService(S filterService) {
		this.filterService = filterService;
	}

	/**
	 * @return
	 */
	public Map<String, Object> getFilterMap() {
		return filterMap;
	}

	/**
	 * @param filterMap
	 */
	public void setFilterMap(Map<String, Object> filterMap) {
		this.filterMap = filterMap;
	}

	/**
	 * @return
	 */
	public LinkedHashMap<String, Direction> getOrderMap() {
		return orderMap;
	}

	/**
	 * @param orderMap
	 */
	public void setOrderMap(LinkedHashMap<String, Direction> orderMap) {
		this.orderMap = orderMap;
	}

	/**
	 * @return the number of current page, starting from 1.
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return quantity of results per page or default
	 *         {@value #DEFAULT_PAGE_SIZE}.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the quantity of results per page
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		if (pageSize < 1) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	/**
	 * @return
	 */
	public PageFilter<E> getPageFilter() {
		return pageFilter;
	}

	/**
	 * @param pageFilter
	 */
	public void setPageFilter(PageFilter<E> pageFilter) {
		this.pageFilter = pageFilter;
	}

}