package javax.persistence.filter.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.repository.JpaFilterRepository;

public interface FilterService<E, ID extends Serializable, R extends JpaFilterRepository> {

	E find(ID id);

	List<E> find(Collection<ID> ids);

	boolean exists(ID id);

	E save(E entity);

	E update(E entity);

	<C extends Collection<E>> C save(C entities);

	List<E> update(Collection<E> entities);

	void delete(E entity);

	void delete(ID id);

	void delete(Collection<? extends E> entities);

	int deleteAll();

	List<E> listAll();

	long countAll();

	PageFilter<E> filter(Filter<E> filter);

	PageFilter<E> filter(Filter<E> filter, int offset, int limit);

	E filterOne(Filter<E> filter);

	List<E> list(Filter<E> filter);

	List<E> list(Filter<E> filter, int offset, int limit);

	long count(Filter<E> filter);

}