package javax.persistence.filter.test.service.impl;

import javax.persistence.filter.service.FilterServiceImpl;
import javax.persistence.filter.test.domain.CountryLanguage;
import javax.persistence.filter.test.domain.CountryLanguagePK;
import javax.persistence.filter.test.repository.CountryLanguageRepository;
import javax.persistence.filter.test.service.CountryLanguageService;

import org.springframework.stereotype.Service;

@Service
public class CountryLanguageServiceImpl extends FilterServiceImpl<CountryLanguage, CountryLanguagePK, CountryLanguageRepository> implements CountryLanguageService {

}