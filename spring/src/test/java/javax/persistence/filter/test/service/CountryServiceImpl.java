package javax.persistence.filter.test.service;

import javax.persistence.filter.service.FilterServiceImpl;
import javax.persistence.filter.test.domain.Country;
import javax.persistence.filter.test.repository.CountryRepository;

import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl extends FilterServiceImpl<Country, String, CountryRepository>
		implements CountryService {

}