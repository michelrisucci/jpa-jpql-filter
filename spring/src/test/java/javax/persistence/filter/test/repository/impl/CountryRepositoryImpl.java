package javax.persistence.filter.test.repository.impl;

import javax.persistence.filter.repository.JpaFilterRepositoryImpl;
import javax.persistence.filter.test.repository.ContinentRepository;

import org.springframework.stereotype.Repository;

@Repository
public class CountryRepositoryImpl extends JpaFilterRepositoryImpl implements ContinentRepository {

}