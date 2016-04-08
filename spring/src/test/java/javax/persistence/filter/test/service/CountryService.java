package javax.persistence.filter.test.service;

import javax.persistence.filter.service.FilterService;
import javax.persistence.filter.test.domain.Country;
import javax.persistence.filter.test.repository.CountryRepository;

public interface CountryService extends FilterService<Country, String, CountryRepository> {

}