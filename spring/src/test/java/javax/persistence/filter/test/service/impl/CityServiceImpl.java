package javax.persistence.filter.test.service.impl;

import javax.persistence.filter.service.FilterDelegatingService;
import javax.persistence.filter.test.domain.City;
import javax.persistence.filter.test.repository.CityRepository;
import javax.persistence.filter.test.service.CityService;

import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl extends FilterDelegatingService<City, Integer, CityRepository> implements CityService {

}