package javax.persistence.filter.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.core.Filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class FilterServiceImpl<E, ID extends Serializable, R extends JpaRepository<E, ID>> extends DefaultServiceImpl<E, ID, R> implements FilterService<E, ID> {

	@Autowired
	private EntityManager manager;

	@Override
	public PageFilter<E> filter(Filter<E> filter) {
		return Filters.filter(manager, filter);
	}

	@Override
	public PageFilter<E> filter(Filter<E> filter, int offset, int limit) {
		return Filters.filter(manager, filter, offset, limit);
	}

	@Override
	public List<E> list(Filter<E> filter) {
		return list(filter, -1, -1);
	}

	@Override
	public List<E> list(Filter<E> filter, int offset, int limit) {
		return Filters.list(manager, filter, offset, limit);
	}

	@Override
	public E filterOne(Filter<E> filter) throws NonUniqueResultException {
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
	public List<E> listAll() {
		return Filters.listAll(manager, getEntityType());
	}

	@Override
	public long count(Filter<E> filter) {
		return Filters.count(manager, filter);
	}

	@Override
	public long countAll() {
		return Filters.countAll(manager, getEntityType());
	}

}