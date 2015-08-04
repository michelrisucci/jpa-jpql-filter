package javax.persistence.filter.repository;

public interface Repository {

	<T, PK> T find(Class<T> type, PK pk);

	<T> void save(T entity);

	<T> void delete(T entity);

	<T> T update(T entity);

}