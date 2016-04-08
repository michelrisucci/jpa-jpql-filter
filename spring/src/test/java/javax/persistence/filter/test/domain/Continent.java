package javax.persistence.filter.test.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Continent {

	@Id
	private String name;
	@OneToMany(mappedBy = "continent")
	private Set<Country> countries;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Country> getCountries() {
		return this.countries;
	}

	public void setCountries(Set<Country> countries) {
		this.countries = countries;
	}

}