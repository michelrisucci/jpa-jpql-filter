package javax.persistence.filter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.filter.core.Filters;
import javax.persistence.filter.core.Order;
import javax.persistence.filter.core.Where;
import javax.persistence.filter.entity.City;

import org.junit.AfterClass;
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
		EM.createQuery("SELECT COUNT(x) FROM Country x").getResultList();
		EM.createQuery("SELECT COUNT(x) FROM City x").getResultList();
		EM.createQuery("SELECT COUNT(x) FROM Continent x").getResultList();
	}

	@AfterClass
	public static void afterClass() {
		EM.close();
		EMF.close();
	}

	@Test
	public void test() {
		long start = System.currentTimeMillis();
		System.out.println("Starting tests...");
		Filter<City> filter = Filter.newInstance(City.class);
		filter.add(Where.iLike("country.continent.name", "a"), Where.iLike("name", "ban"));
		filter.add(Order.ascending("country.continent.name"), Order.ascending("name"));

		long nano = System.nanoTime();
		PageFilter<City> results = Filters.filter(EM, filter);
		System.out.println(">>> NANO TIME: " + (System.nanoTime() - nano));

		System.out.println("Results: " + results.getCount());
		for (City city : results.getList()) {
			System.out.println(city + " - " + city.getCountry().getContinent().getName());
		}
		System.out.println("Ending tests...  " + (System.currentTimeMillis() - start) + " milliseconds.");
	}

}