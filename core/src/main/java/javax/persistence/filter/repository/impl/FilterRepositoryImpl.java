package javax.persistence.filter.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.filter.Filter;
import javax.persistence.filter.Filterable;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.core.Filters;
import javax.persistence.filter.repository.Repository;

public class FilterRepositoryImpl implements Repository, Filterable {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public <T> PageFilter<T> filter(Filter<T> filter, int offset, int limit) {
		return Filters.filter(entityManager, filter, offset, limit);
	}

	public <T, PK> T find(Class<T> type, PK pk) {
		return entityManager.find(type, pk);
	}

	public <T> void save(T entity) {
		entityManager.persist(entity);
	}

	public <T> void delete(T entity) {
		entityManager.remove(entity);
	}

	public <T> T update(T entity) {
		return entityManager.merge(entity);
	}

}