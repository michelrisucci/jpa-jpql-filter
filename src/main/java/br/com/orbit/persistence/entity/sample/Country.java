package br.com.orbit.persistence.entity.sample;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.orbit.persistence.entity.VersionedIdentity;

/**
 * @author Michel Risucci
 */
@Entity
@Table(name = "country", indexes = { @Index(name = "ix_country_surface_area", columnList = "surface_area") }, uniqueConstraints = {
		@UniqueConstraint(name = "uq_country_iso_3166_one_2", columnNames = "iso_3166_one_2"),
		@UniqueConstraint(name = "uq_country_iso_3166_one_3", columnNames = "iso_3166_one_3"),
		@UniqueConstraint(name = "uq_country_iso_3166_int_name", columnNames = "international_name"),
		@UniqueConstraint(name = "uq_country_iso_3166_loc_name", columnNames = "local_name") })
public class Country extends VersionedIdentity<Integer> {

	private String iso3166One2;
	private String iso3166One3;
	private String internationalName;
	private String localName;
	private Region region;
	private double surfaceArea;
	private List<District> districts;
	private List<City> cities;

	/**
	 * 
	 */
	public Country() {
	}

	/**
	 * @return
	 */
	@Basic(optional = false)
	@Column(name = "iso_3166_one_2", nullable = false, length = 2)
	public String getIso3166One2() {
		return iso3166One2;
	}

	/**
	 * @param iso3166One2
	 */
	public void setIso3166One2(String iso3166One2) {
		this.iso3166One2 = iso3166One2;
	}

	/**
	 * @return
	 */
	@Basic(optional = false)
	@Column(name = "iso_3166_one_3", nullable = false, length = 3)
	public String getIso3166One3() {
		return iso3166One3;
	}

	/**
	 * @param iso3166One3
	 */
	public void setIso3166One3(String iso3166One3) {
		this.iso3166One3 = iso3166One3;
	}

	/**
	 * @return
	 */
	@Basic(optional = false)
	@Column(name = "international_name", nullable = false, length = 64)
	public String getInternationalName() {
		return internationalName;
	}

	/**
	 * @param internationalName
	 */
	public void setInternationalName(String internationalName) {
		this.internationalName = internationalName;
	}

	/**
	 * @return
	 */
	@Basic(optional = false)
	@Column(name = "local_name", nullable = false, length = 64)
	public String getLocalName() {
		return localName;
	}

	/**
	 * @param localName
	 */
	public void setLocalName(String localName) {
		this.localName = localName;
	}

	/**
	 * @return
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "region_id", nullable = false, foreignKey = @ForeignKey(name = "fk_country_region"))
	public Region getRegion() {
		return region;
	}

	/**
	 * @param region
	 */
	public void setRegion(Region region) {
		this.region = region;
	}

	/**
	 * @return
	 */
	@Basic(optional = true)
	@Column(name = "surface_area", nullable = true)
	public double getSurfaceArea() {
		return surfaceArea;
	}

	/**
	 * @param surfaceArea
	 */
	public void setSurfaceArea(double surfaceArea) {
		this.surfaceArea = surfaceArea;
	}

	/**
	 * @return
	 */
	@OneToMany(mappedBy = "country")
	public List<District> getDistricts() {
		return districts;
	}

	/**
	 * @param districts
	 */
	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}

	/**
	 * @return
	 */
	@OneToMany(mappedBy = "country")
	public List<City> getCities() {
		return cities;
	}

	/**
	 * @param cities
	 */
	public void setCities(List<City> cities) {
		this.cities = cities;
	}

}