package javax.persistence.filter.test.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CountryLanguage {

	@EmbeddedId
	private CountryLanguagePK id;
	private Boolean isOfficial;
	private float percentage;
	@ManyToOne
	@JoinColumn(name = "countryCode", insertable = false, updatable = false)
	private Country country;

	public CountryLanguagePK getId() {
		return this.id;
	}

	public void setId(CountryLanguagePK id) {
		this.id = id;
	}

	public Boolean getIsOfficial() {
		return this.isOfficial;
	}

	public void setIsOfficial(Boolean isOfficial) {
		this.isOfficial = isOfficial;
	}

	public float getPercentage() {
		return this.percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}