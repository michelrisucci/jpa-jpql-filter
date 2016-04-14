package javax.persistence.filter.test.repository.impl;

import javax.persistence.filter.repository.JpaFilterRepositoryImpl;
import javax.persistence.filter.test.repository.CountryRepository;

import org.springframework.stereotype.Repository;

@Repository
public class ContinentRepositoryImpl extends JpaFilterRepositoryImpl implements CountryRepository {

}