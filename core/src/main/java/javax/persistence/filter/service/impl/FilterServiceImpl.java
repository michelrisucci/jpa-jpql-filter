package javax.persistence.filter.service.impl;

import javax.persistence.filter.Filter;
import javax.persistence.filter.Filterable;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.repository.Repository;
import javax.persistence.filter.service.Service;

public class FilterServiceImpl<T, PK, R extends Repository & Filterable>
		implements Service<T, PK, R>, Filterable {

	protected R repository;

	@Override
	@SuppressWarnings("hiding")
	public <T> PageFilter<T> filter(Filter<T> filter, int offset, int limit) {
		return repository.filter(filter, offset, limit);
	}

	@Override
	public T find(Class<T> type, PK pk) {
		return repository.find(type, pk);
	}

	@Override
	public void save(T entity) {
		repository.save(entity);
	}

	@Override
	public void delete(T entity) {
		repository.delete(entity);
	}

	@Override
	public T update(T entity) {
		return repository.update(entity);
	}

}