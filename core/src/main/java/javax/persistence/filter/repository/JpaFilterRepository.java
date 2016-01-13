package javax.persistence.filter.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;

public interface JpaFilterRepository {

	<E, ID extends Serializable> E find(Class<E> type, ID id);

	<E, ID extends Serializable> List<E> find(Class<E> type, Collection<ID> ids);

	<E, ID extends Serializable> boolean exists(Class<E> type, ID id);

	<E> E save(E entity);

	<E> E update(E entity);

	<E, C extends Collection<E>> C save(C entities);

	<E> List<E> updateAll(Collection<E> entities);

	<E> void delete(E entity);

	<E, ID extends Serializable> void delete(Class<E> type, ID id);

	<E> int deleteAll(Class<E> type);

	void flush();

	<E> PageFilter<E> filter(Filter<E> filter);

	<E> PageFilter<E> filter(Filter<E> filter, int offset, int limit);

	<E> List<E> list(Filter<E> filter);

	<E> List<E> list(Filter<E> filter, int offset, int limit);

	<E> E filterOne(Filter<E> filter);

	<E> List<E> listAll(Class<E> type);

	<E> long count(Filter<E> filter);

	<E> long countAll(Class<E> type);

	<ID extends Serializable, E> ID getId(E entity, Class<ID> idType);

	<ID extends Serializable, E> String getIdPropertyName(Class<E> entityType, Class<ID> idType);

}