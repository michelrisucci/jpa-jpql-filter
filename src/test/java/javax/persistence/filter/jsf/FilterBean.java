package javax.persistence.filter.jsf;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.exception.FirstResultOutOfRangeException;
import javax.persistence.filter.service.FilterService;

public abstract class FilterBean<E, S extends FilterService<E>> {

	protected static final int DEFAULT_MAX_RESULTS = 20;

	// Services
	protected S service;

	// Filter
	protected Map<String, Object> filterMap;
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
		Filter<E> filtro = eraseFilter();
		this.pageFilter = doFilter(filtro, currentPage, results, filterMap);
	}

	/**
	 * 
	 */
	public void doFilter() {
		Filter<E> filtro = newFilter();
		this.pageFilter = doFilter(filtro, currentPage, results, filterMap);
	}

	/**
	 * Low-level filter method that processes filter map.
	 * 
	 * @param filter
	 * @param pagina
	 * @param qtdReg
	 * @param map
	 * @return
	 */
	protected PageFilter<E> doFilter(Filter<E> filter, int pagina, int qtdReg,
			Map<String, Object> map) {

		// Adicionando funções do filtro;
		this.addWheres(filter, map);
		this.addOrders(filter);

		// Filtrando
		return doFilter(filter, pagina, qtdReg);
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
	protected abstract void addWheres(Filter<E> filtro, Map<String, Object> mapa);

	/**
	 * Adds filter ordering clauses;
	 */
	protected abstract void addOrders(Filter<E> filtro);

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
		if (filterMap == null) {
			filterMap = new HashMap<String, Object>();
		} else {
			filterMap.clear();
		}

		this.currentPage = 1;
		this.results = results;

		return newFilter();
	}

}