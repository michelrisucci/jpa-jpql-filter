package javax.persistence.filter.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "country")
public class Country {

	private String code;
	private String name;
	private Continent continent;
	private Long population;
	private Double lifeExpectancy;
	private List<City> cities;

	@Id
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name = "continent")
	public Continent getContinent() {
		return continent;
	}

	public void setContinent(Continent continent) {
		this.continent = continent;
	}

	public Long getPopulation() {
		return population;
	}
	
	public void setPopulation(Long population) {
		this.population = population;
	}

	public Double getLifeExpectancy() {
		return lifeExpectancy;
	}
	
	public void setLifeExpectancy(Double lifeExpectancy) {
		this.lifeExpectancy = lifeExpectancy;
	}

	@Override
	public String toString() {
		return code + " " + name + " (" + continent.getName() + ")";
	}

	@OneToMany(mappedBy = "country")
	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

}