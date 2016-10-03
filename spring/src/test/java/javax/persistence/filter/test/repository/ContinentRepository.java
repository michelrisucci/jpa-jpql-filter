package javax.persistence.filter.test.repository;

import javax.persistence.filter.test.domain.Continent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContinentRepository extends JpaRepository<Continent, String> {

}
