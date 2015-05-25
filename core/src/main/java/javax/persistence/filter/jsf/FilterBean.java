package javax.persistence.filter.jsf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.core.Order.Direction;
import javax.persistence.filter.exception.FirstResultOutOfRangeException;
import javax.persistence.filter.service.FilterService;

public abstract class FilterBean<E, S extends FilterService<E>> {

	protected static final int DEFAULT_MAX_RESULTS = 20;

	// Services
	protected S service;

	// Filter
	protected Map<String, Object> filterMap;
	protected Map<String, Direction> orderMap;
	protected int currentPage;
	protected int results;
	protected PageFilter<E> pageFilter;

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
		this.pageFilter = doFilter(filter, //
				currentPage, results, filterMap, orderMap);
	}

	/**
	 * 
	 */
	public void doFilter() {
		Filter<E> filter = newFilter();
		this.pageFilter = doFilter(filter, //
				currentPage, results, filterMap, orderMap);
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
	protected PageFilter<E> doFilter(Filter<E> filter, int page, int qtdReg,
			Map<String, Object> fMap, Map<String, Direction> oMap) {

		// Adding filter clauses;
		this.addWheres(filter, fMap);

		// Adding filter orders
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Direction> oLink = LinkedHashMap.class.cast(oMap);
		Iterator<Entry<String, Direction>> oIter = oLink.entrySet().iterator();
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
			return service.filter(filter, initial, results);
		} catch (FirstResultOutOfRangeException e) {
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
	protected void addOrders(Filter<E> filter,
			Iterator<Entry<String, Direction>> iterator) {
		while (iterator.hasNext()) {
			Entry<String, Direction> entry = iterator.next();
			Direction direction = entry.getValue();
			String path = entry.getKey();
			filter.add(direction.createOrder(path));
		}
	}

	/**
	 * @return
	 */
	protected abstract Filter<E> newFilter();

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

}