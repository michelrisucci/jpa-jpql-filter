package javax.persistence.filter.service.impl;

import javax.inject.Inject;
import javax.persistence.filter.Filter;
import javax.persistence.filter.Filterable;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.repository.Repository;
import javax.persistence.filter.service.FilterableService;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

public class FilterServiceImpl<T, PK, R extends Repository & Filterable>
		implements FilterableService<T, PK, R> {

	@Inject
	protected R repository;

	@Override
	@SuppressWarnings("hiding")
	public <T> PageFilter<T> filter(Filter<T> filter, int offset, int limit) {
		return repository.filter(filter, offset, limit);
	}

	@Override
	@Transactional(TxType.REQUIRED)
	public T find(Class<T> type, PK pk) {
		return repository.find(type, pk);
	}

	@Override
	@Transactional(TxType.REQUIRED)
	public void save(T entity) {
		repository.save(entity);
	}

	@Override
	@Transactional(TxType.REQUIRED)
	public void delete(T entity) {
		repository.delete(entity);
	}

	@Override
	@Transactional(TxType.REQUIRED)
	public T update(T entity) {
		return repository.update(entity);
	}

}