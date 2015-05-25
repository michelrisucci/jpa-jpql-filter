package javax.persistence.filter.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.entity.VersionedIdentity;

/**
 * @author Michel Risucci
 */
@Entity
@Table(name = "country_language")
public class CountryLanguage extends VersionedIdentity<Integer> {

	private String language;
	private Country country;
	private boolean official;
	private double populationShare;

	/**
	 * 
	 */
	public CountryLanguage() {
	}

	/**
	 * @return
	 */
	@Basic(optional = false)
	@Column(name = "language", nullable = false, length = 32)
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "country_id", nullable = false, foreignKey = @ForeignKey(name = "fk_country_lang_country"))
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
	@Basic(optional = false)
	@Column(name = "is_official", nullable = false)
	public boolean isOfficial() {
		return official;
	}

	/**
	 * @param official
	 */
	public void setOfficial(boolean official) {
		this.official = official;
	}

	/**
	 * @return
	 */
	@Basic(optional = false)
	@Column(name = "population_share", nullable = false)
	public double getPopulationShare() {
		return populationShare;
	}

	/**
	 * @param populationShare
	 */
	public void setPopulationShare(double populationShare) {
		this.populationShare = populationShare;
	}

}