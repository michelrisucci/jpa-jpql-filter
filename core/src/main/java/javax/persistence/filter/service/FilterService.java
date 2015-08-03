package javax.persistence.filter.service;

import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;

public interface FilterService {

	/**
	 * Filters any defined entity.
	 * 
	 * @param filter
	 * @param offset
	 * @param limit
	 * @return
	 */
	<T> PageFilter<T> filter(Filter<T> filter, int offset, int limit);

}