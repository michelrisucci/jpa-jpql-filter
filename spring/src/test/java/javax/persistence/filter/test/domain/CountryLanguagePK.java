package javax.persistence.filter.test.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CountryLanguagePK implements Serializable {

	private static final long serialVersionUID = 4954898936534429553L;

	@Column(insertable = false, updatable = false)
	private String countryCode;
	private String language;

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CountryLanguagePK)) {
			return false;
		}
		CountryLanguagePK castOther = (CountryLanguagePK) other;
		return this.countryCode.equals(castOther.countryCode) && this.language.equals(castOther.language);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.countryCode.hashCode();
		hash = hash * prime + this.language.hashCode();

		return hash;
	}

}