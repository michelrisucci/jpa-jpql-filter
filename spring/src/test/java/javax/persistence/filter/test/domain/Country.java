package javax.persistence.filter.test.domain;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Country {

	@Id
	private String code;
	private String code2;
	private BigDecimal gnp;
	private BigDecimal gnpOld;
	private String governmentForm;
	private String headOfState;
	private Integer indepYear;
	private Float lifeExpectancy;
	private String localName;
	private String name;
	private Integer population;
	private String region;
	private float surfaceArea;
	@OneToMany(mappedBy = "country")
	private Set<City> cities;
	@ManyToOne
	@JoinColumn(name = "capital")
	private City city;
	@ManyToOne
	@JoinColumn(name = "continent")
	private Continent continent;
	@OneToMany(mappedBy = "country")
	private Set<CountryLanguage> languages;

	public Country() {
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode2() {
		return this.code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public BigDecimal getGnp() {
		return this.gnp;
	}

	public void setGnp(BigDecimal gnp) {
		this.gnp = gnp;
	}

	public BigDecimal getGnpOld() {
		return this.gnpOld;
	}

	public void setGnpOld(BigDecimal gnpold) {
		this.gnpOld = gnpold;
	}

	public String getGovernmentForm() {
		return this.governmentForm;
	}

	public void setGovernmentForm(String governmentForm) {
		this.governmentForm = governmentForm;
	}

	public String getHeadOfState() {
		return this.headOfState;
	}

	public void setHeadOfState(String headOfState) {
		this.headOfState = headOfState;
	}

	public Integer getIndepYear() {
		return this.indepYear;
	}

	public void setIndepYear(Integer indepYear) {
		this.indepYear = indepYear;
	}

	public Float getLifeExpectancy() {
		return this.lifeExpectancy;
	}

	public void setLifeExpectancy(Float lifeExpectancy) {
		this.lifeExpectancy = lifeExpectancy;
	}

	public String getLocalName() {
		return this.localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPopulation() {
		return this.population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public float getSurfacearea() {
		return this.surfaceArea;
	}

	public void setSurfacearea(float surfacearea) {
		this.surfaceArea = surfacearea;
	}

	public Set<City> getCities() {
		return this.cities;
	}

	public void setCities(Set<City> cities) {
		this.cities = cities;
	}

	public City getCity() {
		return this.city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Continent getContinent() {
		return this.continent;
	}

	public void setContinent(Continent continent) {
		this.continent = continent;
	}

	public Set<CountryLanguage> getLanguages() {
		return this.languages;
	}

	public void setLanguages(Set<CountryLanguage> countryLanguages) {
		this.languages = countryLanguages;
	}

}