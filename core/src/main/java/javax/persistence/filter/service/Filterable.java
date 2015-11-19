package javax.persistence.filter.service;

import java.util.List;

import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;

public interface Filterable<T> {

	PageFilter<T> filter(Filter<T> filter);

	PageFilter<T> filter(Filter<T> filter, int offset, int limit);

	List<T> list(Filter<T> filter, int offset, int limit);

	List<T> listAll(Class<T> type);

	long count(Filter<T> filter);

	long countAll(Class<T> type);

}