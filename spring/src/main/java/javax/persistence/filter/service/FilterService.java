package javax.persistence.filter.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NonUniqueResultException;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;

public interface FilterService<E, ID extends Serializable> extends DataService<E, ID> {

	PageFilter<E> filter(Filter<E> filter);

	PageFilter<E> filter(Filter<E> filter, int offset, int limit);

	List<E> list(Filter<E> filter);

	List<E> list(Filter<E> filter, int offset, int limit);

	E filterOne(Filter<E> filter) throws NonUniqueResultException;

	long count(Filter<E> filter);

}
