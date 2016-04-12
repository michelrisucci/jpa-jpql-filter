package javax.persistence.filter.test.service;

import javax.persistence.filter.service.FilterService;
import javax.persistence.filter.test.domain.CountryLanguage;
import javax.persistence.filter.test.domain.CountryLanguagePK;
import javax.persistence.filter.test.repository.CountryLanguageRepository;

public interface CountryLanguageService extends FilterService<CountryLanguage, CountryLanguagePK, CountryLanguageRepository> {

}