package javax.persistence.filter.test.repository;

import javax.persistence.filter.test.domain.City;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {

}
