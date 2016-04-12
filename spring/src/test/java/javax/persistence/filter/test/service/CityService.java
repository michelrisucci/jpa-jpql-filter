package javax.persistence.filter.test.service;

import javax.persistence.filter.service.FilterService;
import javax.persistence.filter.test.domain.City;
import javax.persistence.filter.test.repository.CityRepository;

public interface CityService extends FilterService<City, Integer, CityRepository> {

}