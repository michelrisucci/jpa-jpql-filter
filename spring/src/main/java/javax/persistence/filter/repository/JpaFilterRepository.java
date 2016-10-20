package javax.persistence.filter.repository;

import java.util.List;

import javax.persistence.NonUniqueResultException;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;

public interface JpaFilterRepository {

	<E> PageFilter<E> filter(Filter<E> filter);

	<E> PageFilter<E> filter(Filter<E> filter, int offset, int limit);

	<E> List<E> list(Filter<E> filter);

	<E> List<E> list(Filter<E> filter, int offset, int limit);

	<E> E filterOne(Filter<E> filter) throws NonUniqueResultException;

	<E> long count(Filter<E> filter);

}
