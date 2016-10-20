package javax.persistence.filter.test.service.impl;

import javax.persistence.filter.service.FilterDelegatingService;
import javax.persistence.filter.test.domain.Continent;
import javax.persistence.filter.test.repository.ContinentRepository;
import javax.persistence.filter.test.service.ContinentService;

import org.springframework.stereotype.Service;

@Service
public class ContinentServiceImpl extends FilterDelegatingService<Continent, String, ContinentRepository> implements ContinentService {

}