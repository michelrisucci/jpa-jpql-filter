package javax.persistence.filter.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.repository.JpaFilterRepository;
import javax.persistence.filter.service.FilterService;
import javax.persistence.utils.ReflectionUtils;

public abstract class FilterServiceImpl<E, ID extends Serializable> implements FilterService<E, ID> {

	private Class<E> entityType;
	private Class<ID> entityIdType;

	@Inject
	private JpaFilterRepository repository;

	@Override
	public E find(Class<E> type, ID id) {
		return getRepository().find(type, id);
	}

	@Override
	public List<E> find(Class<E> type, Collection<ID> ids) {
		return getRepository().find(type, ids);
	}

	@Override
	public boolean exists(Class<E> type, ID id) {
		return getRepository().exists(type, id);
	}

	@Override
	public E save(E entity) {
		return getRepository().save(entity);
	}

	@Override
	public E update(E entity) {
		return getRepository().update(entity);
	}

	@Override
	public <C extends Collection<E>> C save(C entities) {
		return getRepository().save(entities);
	}

	@Override
	public List<E> update(Collection<E> entities) {
		return getRepository().updateAll(entities);
	}

	@Override
	public void delete(E entity) {
		getRepository().delete(entity);
	}

	@Override
	public void delete(Class<E> type, ID id) {
		getRepository().delete(type, id);
	}

	@Override
	public void delete(Collection<? extends E> entities) {
		getRepository().delete(entities);
	}

	@Override
	public int deleteAll(Class<E> type) {
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
	public long count(Filter<E> filter) {
		return getRepository().count(filter);
	}

	@Override
	public long countAll(Class<E> type) {
		return getRepository().countAll(type);
	}

	@SuppressWarnings("unchecked")
	protected Class<E> getEntityType() {
		if (entityType == null) {
			entityType = (Class<E>) ReflectionUtils.getParameterType(this, 0);
		}
		return entityType;
	}

	@SuppressWarnings("unchecked")
	protected Class<ID> getEntityIdType() {
		if (entityIdType == null) {
			entityIdType = (Class<ID>) ReflectionUtils.getParameterType(this, 1);
		}
		return entityIdType;
	}

	protected JpaFilterRepository getRepository() {
		return repository;
	}

}