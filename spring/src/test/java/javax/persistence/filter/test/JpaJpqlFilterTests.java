package javax.persistence.filter.test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.filter.Filter;
import javax.persistence.filter.core.Order;
import javax.persistence.filter.core.Where;
import javax.persistence.filter.test.context.Database;
import javax.persistence.filter.test.context.Hibernate;
import javax.persistence.filter.test.context.Jpa;
import javax.persistence.filter.test.domain.Country;
import javax.persistence.filter.test.domain.CountryLanguage;
import javax.persistence.filter.test.service.CityService;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, //
		classes = { JpaJpqlFilterTests.class, Database.class, Jpa.class, Hibernate.class })
@Configuration
@ComponentScan(basePackages = "javax.persistence.filter")
@EnableTransactionManagement
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
		filter.add(Where.equal("id.language", "English"));
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
		final String iStartsWithVal = "NeW";

		Filter<CountryLanguage> filter = Filter.newInstance(CountryLanguage.class);
		filter.add(Where.iStartsWith("country.capital.name", iStartsWithVal));

		List<CountryLanguage> results = countryLanguageService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (CountryLanguage result : results) {
			String name = result.getCountry().getCapital().getName();
			Assert.assertTrue(name.toUpperCase().startsWith(iStartsWithVal.toUpperCase()));
		}
	}

	@Test
	public void lesserThan() {
		final BigDecimal lesserThanVal = new BigDecimal("0.1");

		Filter<CountryLanguage> filter = Filter.newInstance(CountryLanguage.class);
		filter.add(Where.lesserThan("percentage", lesserThanVal));
		filter.add(Order.byDescending("percentage"));

		List<CountryLanguage> results = countryLanguageService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (CountryLanguage result : results) {
			Assert.assertTrue(lesserThanVal.compareTo(result.getPercentage()) == 1);
		}
	}

	@Test
	public void lesserThanOrEqual() {
		final BigDecimal lesserThanOrEqualVal = new BigDecimal("0.1");

		Filter<CountryLanguage> filter = Filter.newInstance(CountryLanguage.class);
		filter.add(Where.lesserThanOrEqual("percentage", lesserThanOrEqualVal));
		filter.add(Order.byDescending("percentage"));

		List<CountryLanguage> results = countryLanguageService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (CountryLanguage result : results) {
			Assert.assertTrue(lesserThanOrEqualVal.compareTo(result.getPercentage()) != -1);
		}
	}

}