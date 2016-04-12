package javax.persistence.filter.test.repository.impl;

import javax.persistence.filter.repository.JpaFilterRepositoryImpl;
import javax.persistence.filter.test.repository.CityRepository;

import org.springframework.stereotype.Repository;

@Repository
public class CityRepositoryImpl extends JpaFilterRepositoryImpl implements CityRepository {

}