package javax.persistence.filter.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.core.Filters;
import javax.persistence.filter.repository.JpaFilterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JpaFilterRepositoryImpl implements JpaFilterRepository {

	private EntityManager em;

	@Autowired
	public JpaFilterRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public <E> PageFilter<E> filter(Filter<E> filter) {
		return Filters.filter(em, filter);
	}

	@Override
	public <E> PageFilter<E> filter(Filter<E> filter, int offset, int limit) {
		return Filters.filter(em, filter, offset, limit);
	}

	@Override
	public <E> List<E> list(Filter<E> filter) {
		return Filters.list(em, filter);
	}

	@Override
	public <E> List<E> list(Filter<E> filter, int offset, int limit) {
		return Filters.list(em, filter, offset, limit);
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
	public <E> long count(Filter<E> filter) {
		return count(filter);
	}

}
