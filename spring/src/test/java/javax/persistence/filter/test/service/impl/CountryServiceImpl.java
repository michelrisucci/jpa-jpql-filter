package javax.persistence.filter.test.service.impl;

import javax.persistence.filter.service.FilterServiceImpl;
import javax.persistence.filter.test.domain.Country;
import javax.persistence.filter.test.service.CountryService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl extends FilterServiceImpl<Country, String, JpaRepository<Country, String>> implements CountryService {

}