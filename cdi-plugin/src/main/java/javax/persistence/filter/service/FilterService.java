package javax.persistence.filter.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface FilterService<E, ID extends Serializable> extends Filterable<E> {

	E find(Class<E> type, ID id);

	List<E> find(Class<E> type, Collection<ID> ids);

	boolean exists(Class<E> type, ID id);

	E save(E entity);

	E update(E entity);

	<C extends Collection<E>> C save(C entities);

	List<E> update(Collection<E> entities);

	void delete(E entity);

	void delete(Class<E> type, ID id);

	void delete(Collection<? extends E> entities);

	int deleteAll(Class<E> type);

}