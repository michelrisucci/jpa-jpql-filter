package br.com.orbit.persistence.entity.sample;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.orbit.persistence.entity.VersionedIdentity;

/**
 * @author Michel Risucci
 */
@Entity
@Table(name = "region", uniqueConstraints = { @UniqueConstraint(name = "uq_region_name", columnNames = "name") })
public class Region extends VersionedIdentity<Short> {

	private String name;
	private List<Country> countries;

	/**
	 * 
	 */
	public Region() {
	}

	/**
	 * @return
	 */
	@Basic(optional = false)
	@Column(name = "name", nullable = false, length = 32)
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
	@OneToMany(mappedBy = "region")
	public List<Country> getCountries() {
		return countries;
	}

	/**
	 * @param countries
	 */
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

}