package javax.persistence.filter;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.filter.core.Filters;
import javax.persistence.filter.core.Where;
import javax.persistence.filter.entity.City;
import javax.persistence.filter.entity.Continent;
import javax.persistence.filter.entity.Country;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tester {

	public static final String AFRICA = "Africa";
	public static final String ANTARCTICA = "Antarctica";
	public static final String ASIA = "Asia";
	public static final String EUROPE = "Europe";
	public static final String NORTH_AMERICA = "North America";
	public static final String OCEANIA = "Oceania";
	public static final String SOUTH_AMERICA = "South America";

	private static EntityManagerFactory EMF;
	private static EntityManager EM;

	private static EntityManagerFactory EMF_C;
	private static EntityManager EM_C;

	@BeforeClass
	public static void beforeClass() {
		EMF = Persistence.createEntityManagerFactory("default");
		EM = EMF.createEntityManager();

		EMF_C = Persistence.createEntityManagerFactory("copy");
		EM_C = EMF_C.createEntityManager();
	}

	@AfterClass
	public static void afterClass() {
		EM.close();
		EMF.close();

		EM_C.close();
		EMF_C.close();
	}

	@Test
	public void test() {
		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iLike("cities.name", "cabedelo"));

		List<Country> countries = Filters.list(EM, filter, 0, 999);

		for (Country country : countries) {
			System.out.println(country);
		}
	}

	public void testCities() {

		final String countryCode = "BRA";

		String jpql = new StringBuilder() //
				.append("SELECT city ") //
				.append("FROM " + Country.class.getSimpleName() + " country ") //
				.append("  INNER JOIN country.cities city ") //
				.append("WHERE country.code = :country ") //
				.append("ORDER BY city.name ASC ") //
				.toString();

		List<City> cities = EM //
				.createQuery(jpql, City.class) //
				.setParameter("country", countryCode) //
				.getResultList();

		for (City c : cities) {
			System.out.println(c);
			Assert.assertEquals(c.getCountry().getCode(), countryCode);
		}
	}

	public void testCountries() {
		final String continent = SOUTH_AMERICA;

		String jpql = new StringBuilder() //
				.append("SELECT c ") //
				.append("FROM " + Continent.class.getSimpleName() + " x ") //
				.append("  INNER JOIN x.countries c ") //
				.append("WHERE x.name = :name ") //
				.append("ORDER BY c.name ASC ") //
				.toString();

		List<Country> countries = EM //
				.createQuery(jpql, Country.class) //
				.setParameter("name", continent) //
				.getResultList();

		for (Country c : countries) {
			System.out.println(c);
			Assert.assertEquals(c.getContinent().getName(), continent);
		}
	}
}