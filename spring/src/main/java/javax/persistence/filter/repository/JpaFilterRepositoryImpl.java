package javax.persistence.filter.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.core.Filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaFilterRepositoryImpl implements JpaFilterRepository {

	private static final String DELETE_ALL_QUERY = "DELETE FROM %s x ";

	@Autowired
	private EntityManager entityManager;

	@Override
	public <E, ID extends Serializable> E find(Class<E> type, ID id) {
		return getEntityManager().find(type, id);
	}

	@Override
	public <E, ID extends Serializable> List<E> find(Class<E> type, Collection<ID> ids) {
		List<E> entities = new ArrayList<E>();
		for (ID id : ids) {
			entities.add(find(type, id));
		}
		return entities;
	}

	@Override
	public <E, ID extends Serializable> boolean exists(Class<E> type, ID id) {
		return find(type, id) != null;
	}

	@Override
	public <E> E save(E entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	@Override
	public <E> E update(E entity) {
		return getEntityManager().merge(entity);
	}

	@Override
	public <E, C extends Collection<E>> C save(C entities) {
		for (E entity : entities) {
			save(entity);
		}
		return entities;
	}

	@Override
	public <E> List<E> updateAll(Collection<E> entities) {
		List<E> updatedEntities = new ArrayList<E>();
		for (E entity : entities) {
			updatedEntities.add(update(entity));
		}
		return updatedEntities;
	}

	@Override
	public <E> void delete(E entity) {
		getEntityManager().remove(entity);
	}

	@Override
	public <E, ID extends Serializable> void delete(Class<E> type, ID id) {
		E entity = find(type, id);
		delete(entity);
	}

	@Override
	public <E> void delete(Collection<? extends E> entities) {
		for (E entity : entities) {
			delete(entity);
		}
	}

	@Override
	public <E> int deleteAll(Class<E> type) {
		String entityName = getEntityName(type);
		String deleteStatement = String.format(DELETE_ALL_QUERY, entityName);
		return getEntityManager().createQuery(deleteStatement).executeUpdate();
	}

	@Override
	public void flush() {
		getEntityManager().flush();
	}

	@Override
	public <E> PageFilter<E> filter(Filter<E> filter) {
		return Filters.filter(getEntityManager(), filter);
	}

	@Override
	public <E> PageFilter<E> filter(Filter<E> filter, int offset, int limit) {
		return Filters.filter(getEntityManager(), filter, offset, limit);
	}

	@Override
	public <E> List<E> list(Filter<E> filter, int offset, int limit) {
		return Filters.list(getEntityManager(), filter, offset, limit);
	}

	@Override
	public <E> List<E> listAll(Class<E> type) {
		return Filters.listAll(getEntityManager(), type);
	}

	@Override
	public <E> long count(Filter<E> filter) {
		return Filters.count(getEntityManager(), filter);
	}

	@Override
	public <E> long countAll(Class<E> type) {
		return Filters.countAll(entityManager, type);
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	protected <E> String getEntityName(Class<E> type) {
		String entityName = type.getSimpleName();
		Entity entity = type.getAnnotation(Entity.class);
		if (entity != null && !entity.name().isEmpty()) {
			entityName = entity.name();
		}
		return entityName;
	}

}