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
@Table(name = "district", uniqueConstraints = { @UniqueConstraint(name = "uq_district_name_country", columnNames = {
		"name", "country_id" }) })
public class District extends VersionedIdentity<Integer> {

	private String name;
	private Country country;

	/**
	 * 
	 */
	public District() {
	}

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
	@ManyToOne(optional = false)
	@JoinColumn(name = "country_id", nullable = false, foreignKey = @ForeignKey(name = "fk_district_country"))
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

}