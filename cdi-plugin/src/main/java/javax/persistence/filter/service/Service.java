package javax.persistence.filter.service;

import javax.persistence.filter.repository.Repository;

public interface Service<T, PK, R extends Repository> {

	T find(Class<T> type, PK pk);

	void save(T entity);

	void delete(Class<T> type, PK pk);

	T update(T entity);

}