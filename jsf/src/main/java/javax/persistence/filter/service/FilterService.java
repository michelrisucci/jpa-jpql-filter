package javax.persistence.filter.service;

import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;

public interface FilterService<E> {

	/**
	 * Filters a specific entity.
	 * 
	 * @param filter
	 * @param startPosition
	 * @param results
	 * @return
	 */
	PageFilter<E> filter(Filter<E> filter, int startPosition, int results);

	/**
	 * Filters any defined entity.
	 * 
	 * @param filter
	 * @param startPosition
	 * @param results
	 * @return
	 */
	<T> PageFilter<T> filterEntity(Filter<T> filter, int startPosition,
			int results);

}