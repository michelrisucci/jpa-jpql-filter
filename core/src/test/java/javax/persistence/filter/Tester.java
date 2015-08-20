package javax.persistence.filter;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.filter.core.Filters;
import javax.persistence.filter.core.Order;
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

	@BeforeClass
	public static void beforeClass() {
		EMF = Persistence.createEntityManagerFactory("default");
		EM = EMF.createEntityManager();
	}

	@AfterClass
	public static void afterClass() {
		EM.close();
		EMF.close();
	}

	@Test
	public void test() {
		System.out.println("Starting tests...");
		Filter<City> filter = Filter.newInstance(City.class);
		filter.setDistinct(true);
		filter.add(
				Where.iLike("country.continent.name", "a"),
				Where.iLike("name", "ban"));
		filter.add(
				Order.ascending("country.continent.name"),
				Order.ascending("name"));

		PageFilter<City> results = Filters.filter(EM, filter);

		System.out.println("Results: " + results.getCount());
		for (City city : results.getList()) {
			System.out.println(city + " - " + city.getCountry().getContinent().getName());
		}
		System.out.println("Ending tests...");
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