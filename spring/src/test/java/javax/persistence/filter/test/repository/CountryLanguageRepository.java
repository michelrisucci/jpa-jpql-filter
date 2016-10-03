package javax.persistence.filter.test.repository;

import javax.persistence.filter.test.domain.CountryLanguage;
import javax.persistence.filter.test.domain.CountryLanguagePK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryLanguageRepository extends JpaRepository<CountryLanguage, CountryLanguagePK> {

}
