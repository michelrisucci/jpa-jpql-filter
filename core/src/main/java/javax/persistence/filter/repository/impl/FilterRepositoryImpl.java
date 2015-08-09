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

	public <T, PK> void delete(Class<T> type, PK pk) {
		T reference = entityManager.getReference(type, pk);
		entityManager.remove(reference);
	}

	public <T> T update(T entity) {
		return entityManager.merge(entity);
	}

}