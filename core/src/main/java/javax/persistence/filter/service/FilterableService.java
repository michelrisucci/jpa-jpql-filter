package javax.persistence.filter.service;

import javax.persistence.filter.Filterable;
import javax.persistence.filter.repository.Repository;

public interface FilterableService<T, PK, R extends Repository & Filterable>
		extends Service<T, PK, R>, Filterable {

}