package javax.persistence.filter.test.repository.impl;

import javax.persistence.filter.repository.JpaFilterRepositoryImpl;
import javax.persistence.filter.test.repository.CountryLanguageRepository;

import org.springframework.stereotype.Repository;

@Repository
public class CountryLanguageRepositoryImpl extends JpaFilterRepositoryImpl implements CountryLanguageRepository {

}