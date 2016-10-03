package javax.persistence.filter.test.repository;

import javax.persistence.filter.test.domain.Country;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, String> {

}
