package javax.persistence.filter.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.repository.JpaFilterRepository;
import javax.persistence.utils.ReflectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED)
public abstract class FilterServiceImpl<E, ID extends Serializable, R extends JpaFilterRepository>
		implements FilterService<E, ID, R> {

	private Class<E> entityType;
	private Class<ID> entityIdType;

	@Autowired
	private R genericRepository;

	@Override
	public E find(ID id) {
		Class<E> type = getEntityType();
		return getRepository().find(type, id);
	}

	@Override
	public List<E> find(Collection<ID> ids) {
		Class<E> type = getEntityType();
		return getRepository().find(type, ids);
	}

	@Override
	public boolean exists(ID id) {
		Class<E> type = getEntityType();
		return getRepository().exists(type, id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public E save(E entity) {
		return getRepository().save(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public E update(E entity) {
		return getRepository().update(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <C extends Collection<E>> C save(C entities) {
		return getRepository().save(entities);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<E> update(Collection<E> entities) {
		return getRepository().updateAll(entities);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(E entity) {
		getRepository().delete(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(ID id) {
		Class<E> type = getEntityType();
		getRepository().delete(type, id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Collection<? extends E> entities) {
		getRepository().delete(entities);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteAll() {
		Class<E> type = getEntityType();
		return getRepository().deleteAll(type);
	}

	@Override
	public PageFilter<E> filter(Filter<E> filter) {
		return getRepository().filter(filter);
	}

	@Override
	public PageFilter<E> filter(Filter<E> filter, int offset, int limit) {
		return getRepository().filter(filter, offset, limit);
	}

	@Override
	public List<E> list(Filter<E> filter, int offset, int limit) {
		return getRepository().list(filter, offset, limit);
	}

	@Override
	public List<E> listAll(Class<E> type) {
		return getRepository().listAll(type);
	}

	@Override
	public List<E> listAll() {
		Class<E> type = getEntityType();
		return listAll(type);
	}

	@Override
	public long count(Filter<E> filter) {
		return getRepository().count(filter);
	}

	@Override
	public long countAll(Class<E> type) {
		return getRepository().countAll(type);
	}

	@Override
	public long countAll() {
		Class<E> type = getEntityType();
		return countAll(type);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@SuppressWarnings("unchecked")
	protected Class<E> getEntityType() {
		if (entityType == null) {
			entityType = (Class<E>) ReflectionUtils.getParameterType(this, 0);
		}
		return entityType;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@SuppressWarnings("unchecked")
	protected Class<ID> getEntityIdType() {
		if (entityIdType == null) {
			entityIdType = (Class<ID>) ReflectionUtils.getParameterType(this, 1);
		}
		return entityIdType;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	protected R getRepository() {
		return genericRepository;
	}

}