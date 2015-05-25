package javax.persistence.filter.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.entity.VersionedIdentity;

/**
 * @author Michel Risucci
 */
@Entity
@Table(name = "city", uniqueConstraints = { @UniqueConstraint(name = "uq_city_name_dtct_country", columnNames = {
		"name", "district_id", "country_id" }) })
public class City extends VersionedIdentity<Long> {

	private String name;
	private District district;
	private Country country;
	private int population;

	/**
	 * @return
	 */
	@Basic(optional = false)
	@Column(name = "name", nullable = false, length = 64)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	@ManyToOne(optional = true)
	@JoinColumn(name = "district_id", nullable = true, foreignKey = @ForeignKey(name = "fk_city_district"))
	public District getDistrict() {
		return district;
	}

	/**
	 * @param district
	 */
	public void setDistrict(District district) {
		this.district = district;
	}

	/**
	 * @return
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "country_id", nullable = false, foreignKey = @ForeignKey(name = "fk_city_country"))
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * @return
	 */
	@Basic(optional = true)
	@Column(name = "population", nullable = true)
	public int getPopulation() {
		return population;
	}

	/**
	 * @param population
	 */
	public void setPopulation(int population) {
		this.population = population;
	}

}