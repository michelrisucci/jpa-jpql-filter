package javax.persistence.filter.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NonUniqueResultException;
import javax.persistence.filter.Filter;
import javax.persistence.filter.PageFilter;
import javax.persistence.filter.repository.JpaFilterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class FilterDelegatingService<E, ID extends Serializable, R extends JpaRepository<E, ID>> extends SimpleDataService<E, ID, R> implements FilterService<E, ID> {

	@Autowired
	private JpaFilterRepository filterRepository;

	@Override
	public PageFilter<E> filter(Filter<E> filter) {
		return filterRepository.filter(filter);
	}

	@Override
	public PageFilter<E> filter(Filter<E> filter, int offset, int limit) {
		return filterRepository.filter(filter, offset, limit);
	}

	@Override
	public List<E> list(Filter<E> filter) {
		return filterRepository.list(filter);
	}

	@Override
	public List<E> list(Filter<E> filter, int offset, int limit) {
		return filterRepository.list(filter, offset, limit);
	}

	@Override
	public E filterOne(Filter<E> filter) throws NonUniqueResultException {
		return filterRepository.filterOne(filter);
	}

	@Override
	public long count(Filter<E> filter) {
		return filterRepository.count(filter);
	}

}