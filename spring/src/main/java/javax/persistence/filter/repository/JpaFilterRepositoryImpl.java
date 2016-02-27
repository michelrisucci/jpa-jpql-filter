package javax.persistence.filter.repository;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.core.Filters;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaFilterRepositoryImpl implements JpaFilterRepository {

	private static final String DELETE_ALL_QUERY = "DELETE FROM %s x ";

	@PersistenceContext
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
	public <E> void save(Collection<E> entities) {
		for (E entity : entities) {
			save(entity);
		}
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
	public <E> List<E> list(Filter<E> filter) {
		return list(filter, -1, -1);
	}

	@Override
	public <E> List<E> list(Filter<E> filter, int offset, int limit) {
		return Filters.list(getEntityManager(), filter, offset, limit);
	}

	@Override
	public <E> E filterOne(Filter<E> filter) throws NonUniqueResultException {
		List<E> list = list(filter, 0, 1);
		if (list.isEmpty()) {
			return null;
		}
		int size = list.size();
		if (size > 1) {
			String message = "Conditionals does not ensure uniqueness: " + size + " results found.";
			throw new NonUniqueResultException(message);
		}
		return list.get(0);
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

	@Override
	@SuppressWarnings("unchecked")
	public <ID extends Serializable, E> ID getId(E entity, Class<ID> idType) {
		String idPropName = getIdPropertyName(entity.getClass(), idType);
		try {
			return (ID) PropertyUtils.getProperty(entity, idPropName);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		// These property accessing runtime exceptions should never be thrown!
	}

	@Override
	public <ID extends Serializable, E> String getIdPropertyName(Class<E> entityType, Class<ID> idType) {
		Metamodel metamodel = getEntityManager().getMetamodel();
		return metamodel.entity(entityType).getId(idType).getName();
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