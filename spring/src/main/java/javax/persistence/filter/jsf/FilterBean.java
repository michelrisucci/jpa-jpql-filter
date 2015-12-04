package javax.persistence.filter.jsf;

import java.io.Serializable;
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
import javax.persistence.filter.repository.JpaFilterRepository;
import javax.persistence.filter.service.FilterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @author Michel Risucci
 *
 * @param <E>
 * @param <F>
 */
public abstract class FilterBean<ID extends Serializable, E, S extends FilterService<E, ID, ? extends JpaFilterRepository>>
		extends SpringBeanAutowiringSupport {

	protected static final int DEFAULT_MAX_RESULTS = 20;

	@Autowired
	protected S filterService;
	// Filter Conditional Params
	protected Map<String, Object> filterMap;
	// Filter Ordinal Params
	protected LinkedHashMap<String, Direction> orderMap;

	// Pagination results
	protected int currentPage;
	protected int results;
	protected PageFilter<E> pageFilter;

	/**
	 * 
	 */
	public FilterBean() {
	}

	@PostConstruct
	protected void postConstruct() {
		eraseAndFilter();
	}

	/**
	 * 
	 */
	public void eraseAndFilter() {
		Filter<E> filter = eraseFilter();
		this.pageFilter = doFilter(filter, currentPage, results, filterMap, orderMap);
	}

	/**
	 * 
	 */
	public void doFilter() {
		Filter<E> filter = newFilter();
		this.pageFilter = doFilter(filter, currentPage, results, filterMap, orderMap);
	}

	/**
	 * Low-level filter method that processes filter map.
	 * 
	 * @param filter
	 * @param page
	 * @param qtdReg
	 * @param fMap
	 * @return
	 */
	protected PageFilter<E> doFilter(Filter<E> filter, int page, int qtdReg, Map<String, Object> fMap,
			LinkedHashMap<String, Direction> oMap) {

		// Adding filter clauses;
		this.addWheres(filter, fMap);

		// Adding filter orders
		Iterator<Entry<String, Direction>> oIter = oMap.entrySet().iterator();
		this.addOrders(filter, oIter);

		// Filtering
		return doFilter(filter, page, qtdReg);
	}

	/**
	 * Low-level filter method.
	 * 
	 * @param filter
	 * @param page
	 * @param results
	 * @return
	 */
	protected PageFilter<E> doFilter(Filter<E> filter, int page, int results) {
		try {
			int initial = (page - 1) * results;
			initial = initial >= 0 ? initial : 0;
			return filterService.filter(filter, initial, results);
		} catch (OffsetOutOfRangeException e) {
			this.currentPage = 1;
			return doFilter(filter, currentPage, results);
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
		if (orderMap.containsKey(path)) {
			Direction current = orderMap.get(path);
			if (Direction.ASC == current) {
				orderMap.put(path, Direction.DESC);
			} else if (Direction.DESC == current) {
				orderMap.put(path, Direction.ASC);
			}
		} else if (direction == null) {
			orderMap.put(path, Direction.ASC);
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
	 * Getters and Setters
	 */

	/**
	 * @return
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
	 * @return
	 */
	public int getResults() {
		return results;
	}

	/**
	 * @param results
	 */
	public void setResults(int results) {
		this.results = results;
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

	/*
	 * Utilities
	 */

	/**
	 * @return
	 */
	protected Filter<E> eraseFilter() {
		return eraseFilter(DEFAULT_MAX_RESULTS);
	}

	/**
	 * @param results
	 * @return
	 */
	protected Filter<E> eraseFilter(int results) {
		// Clearing filter
		if (filterMap == null) {
			filterMap = new HashMap<String, Object>();
		} else {
			filterMap.clear();
		}

		// Clearing orders
		if (orderMap == null) {
			orderMap = new LinkedHashMap<String, Direction>();
		} else {
			orderMap.clear();
		}

		this.currentPage = 1;
		this.results = results;

		return newFilter();
	}

	@SuppressWarnings("unchecked")
	protected Filter<E> newFilter() {
		Type superType = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = ParameterizedType.class.cast(superType);
		Class<E> entityType = Class.class.cast(paramType.getActualTypeArguments()[0]);
		return Filter.newInstance(entityType);
	}

}