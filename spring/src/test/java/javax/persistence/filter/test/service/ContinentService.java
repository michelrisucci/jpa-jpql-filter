package javax.persistence.filter.test.service;

import javax.persistence.filter.service.FilterService;
import javax.persistence.filter.test.domain.Continent;
import javax.persistence.filter.test.repository.ContinentRepository;

public interface ContinentService extends FilterService<Continent, String, ContinentRepository> {

}