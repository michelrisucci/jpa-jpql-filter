package javax.persistence.filter.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface DefaultService<E, ID extends Serializable> {

	List<E> findAll();

	List<E> findAll(Sort sort);

	List<E> findAll(Iterable<ID> ids);

	<S extends E> List<S> save(Iterable<S> entities);

	void deleteInBatch(Iterable<E> entities);

	void deleteAllInBatch();

	E getOne(ID id);

	<S extends E> List<S> findAll(Example<S> example);

	<S extends E> List<S> findAll(Example<S> example, Sort sort);

	Page<E> findAll(Pageable pageable);

	<S extends E> S save(S entity);

	<S extends E> S saveAndFlush(S entity);

	<S extends E> S saveAndCommit(S entity);

	E findOne(ID id);

	boolean exists(ID id);

	long count();

	void delete(ID id);

	void deleteAndFlush(ID id);

	void deleteAndCommit(ID id);

	void delete(Iterable<? extends E> entities);

	void deleteAll();

	<S extends E> S findOne(Example<S> example);

	<S extends E> Page<S> findAll(Example<S> example, Pageable pageable);

	<S extends E> long count(Example<S> example);

	<S extends E> boolean exists(Example<S> example);

}
