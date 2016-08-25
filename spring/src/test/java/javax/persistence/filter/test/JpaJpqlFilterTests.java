package javax.persistence.filter.test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.filter.Filter;
import javax.persistence.filter.core.Order;
import javax.persistence.filter.core.PassthroughRawClause;
import javax.persistence.filter.core.Where;
import javax.persistence.filter.core.conditional.exact.Exact.Operation;
import javax.persistence.filter.test.context.Database;
import javax.persistence.filter.test.context.Hibernate;
import javax.persistence.filter.test.context.Jpa;
import javax.persistence.filter.test.domain.City;
import javax.persistence.filter.test.domain.Continent;
import javax.persistence.filter.test.domain.Country;
import javax.persistence.filter.test.domain.CountryLanguage;
import javax.persistence.filter.test.service.CityService;
import javax.persistence.filter.test.service.ContinentService;
import javax.persistence.filter.test.service.CountryLanguageService;
import javax.persistence.filter.test.service.CountryService;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { JpaJpqlFilterTests.class, Database.class, Jpa.class, Hibernate.class })
@Configuration
@ComponentScan(basePackages = "javax.persistence.filter")
public class JpaJpqlFilterTests {

	private static boolean isDatabaseStarted;

	@Autowired
	private DataSource dataSource;
	@Autowired
	@Value("classpath:world.sql")
	private Resource script;
	@Autowired
	private CountryService countryService;
	@Autowired
	private CountryLanguageService countryLanguageService;
	@Autowired
	private ContinentService continentService;
	@Autowired
	private CityService cityService;

	@Before
	public void startDatabase() throws ScriptException, SQLException {
		if (!isDatabaseStarted) {
			isDatabaseStarted = true;
			ScriptUtils.executeSqlScript(dataSource.getConnection(), script);
		}
	}

	@Test
	public void between() {
		final BigDecimal lesserVal = new BigDecimal("35.0");
		final BigDecimal greaterVal = new BigDecimal("50.0");

		Filter<CountryLanguage> filter = Filter.newInstance(CountryLanguage.class);
		/*
		 * Using PSEUDO separator "!" instead of "." to prevent unnecessary
		 * joins on composite IDs.
		 */
		filter.add(Where.equal("id!language", "English"));
		filter.add(Where.between("country.lifeExpectancy", lesserVal, greaterVal));

		List<CountryLanguage> results = countryLanguageService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (CountryLanguage result : results) {
			int lesserComparison = lesserVal.compareTo(result.getCountry().getLifeExpectancy());
			int greaterComparison = greaterVal.compareTo(result.getCountry().getLifeExpectancy());
			Assert.assertTrue(lesserComparison != 1 && greaterComparison != -1);
		}
	}

	@Test
	public void endsWith() {
		final String val = "States";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.endsWith("name", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getName().endsWith(val));
		}
	}

	@Test
	public void equalString() {
		final String val = "New York";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.equal("cities.name", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertTrue(results.size() == 1);
		for (Country result : results) {
			Assert.assertTrue(result.getCode().equals("USA"));
		}
	}

	@Test
	public void equalNumber() {
		final Number val = Integer.valueOf(8008278);

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.equal("cities.population", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertTrue(results.size() == 1);
		for (Country result : results) {
			Assert.assertTrue(result.getCode().equals("USA"));
		}
	}

	@Test
	public void greaterThan() {
		final BigDecimal val = new BigDecimal("9970610.0");

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.greaterThan("surfaceArea", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		Assert.assertTrue(results.size() == 2);
		for (Country result : results) {
			Assert.assertTrue(val.compareTo(result.getSurfacearea()) == -1);
		}
	}

	@Test
	public void greaterThanOrEqual() {
		final BigDecimal val = new BigDecimal("9970610.0");

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.greaterThanOrEqual("surfaceArea", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		Assert.assertTrue(results.size() == 3);
		for (Country result : results) {
			Assert.assertTrue(val.compareTo(result.getSurfacearea()) != 1);
		}
	}

	@Test
	public void iEndsWith() {
		final String val = "sTaTeS";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iEndsWith("name", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			String name = result.getName();
			Assert.assertTrue(name.toUpperCase().endsWith(val.toUpperCase()));
		}
	}

	@Test
	public void iEqual() {
		final String val = "UsA";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iEqual("code", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertTrue(results.size() == 1);
		for (Country result : results) {
			Assert.assertTrue(result.getCode().toUpperCase().equals(val.toUpperCase()));
		}
	}

	@Test
	public void iLike() {
		final String val = "rEpUbLiC";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iLike("name", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getName().toUpperCase().contains(val.toUpperCase()));
		}
	}

	@Test
	public void iLikeAny() {
		final String[] valArray = { "RePuBlIc", "dEmOcRaTiC" };

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iLikeAny("name", valArray));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			String nameUpper = result.getName().toUpperCase();
			boolean containsAny = false;
			for (String val : valArray) {
				containsAny |= nameUpper.contains(val.toUpperCase());
			}
			Assert.assertTrue(containsAny);
		}
	}

	@Test
	public void inString() {
		final String[] valArray = { "USA", "GBR", "DEU" };

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.in("code", valArray));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertTrue(results.size() == valArray.length);
		for (Country result : results) {
			String code = result.getCode();
			boolean isInVal = false;
			for (String val : valArray) {
				isInVal |= code.equals(val);
			}
			Assert.assertTrue(isInVal);
		}
	}

	@Test
	public void inNumber() {
		final Number[] valArray = { 170115000, 15211000, 98881000 };

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.in("population", valArray));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertTrue(results.size() == valArray.length);
		for (Country result : results) {
			Number population = result.getPopulation();
			boolean isInVal = false;
			for (Number val : valArray) {
				isInVal |= population.equals(val);
			}
			Assert.assertTrue(isInVal);
		}
	}

	@Test
	public void iNotLike() {
		final String val = "RePuBlIc";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iNotLike("name", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertFalse(result.getName().toUpperCase().contains(val.toUpperCase()));
		}
	}

	@Test
	public void isNotNull() {
		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.isNotNull("capital"));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertNotNull(result.getCapital());
		}
	}

	@Test
	public void isNull() {
		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.isNull("capital"));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertNull(result.getCapital());
		}
	}

	@Test
	public void iStartsWith() {
		final String val = "NeW";

		Filter<CountryLanguage> filter = Filter.newInstance(CountryLanguage.class);
		filter.add(Where.iStartsWith("country.capital.name", val));

		List<CountryLanguage> results = countryLanguageService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (CountryLanguage result : results) {
			String name = result.getCountry().getCapital().getName();
			Assert.assertTrue(name.toUpperCase().startsWith(val.toUpperCase()));
		}
	}

	@Test
	public void lesserThan() {
		final BigDecimal val = new BigDecimal("0.1");

		Filter<CountryLanguage> filter = Filter.newInstance(CountryLanguage.class);
		filter.add(Where.lesserThan("percentage", val));
		filter.add(Order.byDescending("percentage"));

		List<CountryLanguage> results = countryLanguageService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (CountryLanguage result : results) {
			Assert.assertTrue(val.compareTo(result.getPercentage()) == 1);
		}
	}

	@Test
	public void lesserThanOrEqual() {
		final BigDecimal val = new BigDecimal("0.1");

		Filter<CountryLanguage> filter = Filter.newInstance(CountryLanguage.class);
		filter.add(Where.lesserThanOrEqual("percentage", val));
		filter.add(Order.byDescending("percentage"));

		List<CountryLanguage> results = countryLanguageService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (CountryLanguage result : results) {
			Assert.assertTrue(val.compareTo(result.getPercentage()) != -1);
		}
	}

	@Test
	public void exactOrNull() {
		final Integer year = 0;

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.lesserThan("indepYear", year).orNull());

		List<Country> results = countryService.filter(filter).getList();
		
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Integer indepYear = result.getIndepYear();
			if (indepYear != null && indepYear >= 0)
				Assert.fail();
		}
	}

	@Test
	public void exactOrEqualOrNull() {
		final Integer year = 0;

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.lesserThanOrEqual("indepYear", year).orNull());

		List<Country> results = countryService.filter(filter).getList();
		
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Integer indepYear = result.getIndepYear();
			if (indepYear != null && indepYear > 0)
				Assert.fail();
		}
	}
	
	@Test
	public void exactOrNullWithJoins() {
		final Integer year = 0;

		Filter<Continent> filter = Filter.newInstance(Continent.class);
		filter.add(Where.lesserThan("countries.indepYear", year).orNull());

		List<Continent> results = continentService.filter(filter).getList();
		
		Assert.assertFalse(results.isEmpty());
		for (Continent result : results) {
			boolean invalid = true;
			for (Country c : result.getCountries()) {
				Integer indepYear = c.getIndepYear();
				if (indepYear == null || indepYear < 0)
					invalid = false;
			}
			
			if (invalid)
				Assert.fail();
		}
	}
	
	@Test
	public void exactOrEqualOrNullWithJoins() {
		final Integer year = 0;

		Filter<Continent> filter = Filter.newInstance(Continent.class);
		filter.add(Where.lesserThanOrEqual("countries.indepYear", year).orNull());

		List<Continent> results = continentService.filter(filter).getList();
		
		Assert.assertFalse(results.isEmpty());
		for (Continent result : results) {
			boolean invalid = true;
			for (Country c : result.getCountries()) {
				Integer indepYear = c.getIndepYear();
				if (indepYear == null || indepYear <= 0)
					invalid = false;
			}
			
			if (invalid)
				Assert.fail();
		}
	}

	@Test
	public void like() {
		final String val = "Republic";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iLike("name", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getName().contains(val));
		}
	}

	@Test
	public void likeAny() {
		final String[] valArray = { "Republic", "Democratic" };

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.likeAny("name", valArray));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			String name = result.getName();
			boolean containsAny = false;
			for (String val : valArray) {
				containsAny |= name.contains(val);
			}
			Assert.assertTrue(containsAny);
		}
	}

	@Test
	public void notEqual() {
		final String val = "South America";

		Filter<Continent> filter = Filter.newInstance(Continent.class);
		filter.add(Where.notEqual("name", val));

		List<Continent> results = continentService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Continent result : results) {
			Assert.assertFalse(result.getName().equals(val));
		}
	}

	@Test
	public void notIn() {
		final String[] valArray = { "Europe", "Oceania", "Asia", "North America", "Africa", "Antarctica" };

		Filter<City> filter = Filter.newInstance(City.class);
		/*
		 * Using PSEUDO separator "!" instead of "." to prevent unnecessary
		 * joins on composite IDs.
		 */
		filter.add(Where.notIn("country!continent!name", valArray));

		List<City> results = cityService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (City result : results) {
			Assert.assertTrue(result.getCountry().getContinent().getName().equals("South America"));
		}
	}

	@Test
	public void notLike() {
		final String val = "Republic";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iNotLike("name", val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertFalse(result.getName().contains(val));
		}
	}

	@Test
	public void startsWith() {
		final String val = "New";

		Filter<CountryLanguage> filter = Filter.newInstance(CountryLanguage.class);
		/*
		 * Using PSEUDO separator "!" instead of "." to prevent unnecessary
		 * joins on composite IDs.
		 */
		filter.add(Where.startsWith("country!capital!name", val));

		List<CountryLanguage> results = countryLanguageService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (CountryLanguage result : results) {
			String name = result.getCountry().getCapital().getName();
			Assert.assertTrue(name.startsWith(val));
		}
	}

	@Test
	public void orderingOne() {
		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iLike("name", "rEpUbLiC"));
		filter.add(Order.byAscending("name"));

		List<Country> results = countryService.filter(filter).getList();
		manualCompareOrdering(results, new Comparator<Country>() {
			@Override
			public int compare(Country o1, Country o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	@Test
	public void orderingMany() {
		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iLike("name", "rEpUbLiC"));
		filter.add(Order.byDescending("continent.name"), Order.byAscending("name"));

		List<Country> results = countryService.filter(filter).getList();
		manualCompareOrdering(results, new Comparator<Country>() {
			public int compare(Country o1, Country o2) {
				// Comparing descending Continent name
				int continentNameDescComparison = o2.getContinent().getName().compareTo(o1.getContinent().getName());
				if (continentNameDescComparison != 0) {
					return continentNameDescComparison;
				}
				// Then comparing Country name
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	private <E> void manualCompareOrdering(List<E> ordered, Comparator<E> manualComparator) {
		// Preparing a new list to reverse the results
		List<E> reversed = new ArrayList<>(ordered);
		Collections.reverse(reversed);
		// Assert that the objects of both lists are not equal.
		Assert.assertFalse(ordered.get(0) == reversed.get(0));
		Assert.assertFalse(ordered.get(ordered.size() - 1) == reversed.get(reversed.size() - 1));

		// Creating a manually sorted set with proper comparator
		SortedSet<E> manuallySorted = new TreeSet<>(manualComparator);
		// Manually sorting results
		manuallySorted.addAll(reversed);

		Iterator<E> orderedIter = ordered.iterator();
		Iterator<E> manuallySortedIter = manuallySorted.iterator();
		while (orderedIter.hasNext()) {
			E expected = orderedIter.next();
			E actual = manuallySortedIter.next();
			// Object reference equality
			Assert.assertTrue(expected == actual);
		}
	}

	@Test
	public void rawJpql() {
		final Number val = Integer.valueOf(140000000);
		final int customAdd = 10000000;
		final int customFinalVal = val.intValue() + customAdd;

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(new PassthroughRawClause("(x.population + " + customAdd + ")", Operation.GREATER_THAN_OR_EQUAL, val));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getPopulation() + customAdd >= customFinalVal);
		}
	}

}