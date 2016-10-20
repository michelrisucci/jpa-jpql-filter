package javax.persistence.filter.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface DataService<E, ID extends Serializable> {

	List<E> findAll();

	List<E> findAll(Sort sort);

	List<E> findAll(Iterable<ID> ids);

	List<E> save(Iterable<E> entities);

	void deleteInBatch(Iterable<E> entities);

	void deleteAllInBatch();

	E getOne(ID id);

	List<E> findAll(Example<E> example);

	List<E> findAll(Example<E> example, Sort sort);

	Page<E> findAll(Pageable pageable);

	E save(E entity);

	E saveAndFlush(E entity);

	E saveAndCommit(E entity);

	E findOne(ID id);

	boolean exists(ID id);

	long count();

	void delete(ID id);

	void deleteAndFlush(ID id);

	void deleteAndCommit(ID id);

	void delete(Iterable<? extends E> entities);

	void deleteAll();

	E findOne(Example<E> example);

	Page<E> findAll(Example<E> example, Pageable pageable);

	long count(Example<E> example);

	boolean exists(Example<E> example);

}
