package javax.persistence.filter.test;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.filter.Filter;
import javax.persistence.filter.core.Where;
import javax.persistence.filter.test.context.Database;
import javax.persistence.filter.test.context.Hibernate;
import javax.persistence.filter.test.context.Jpa;
import javax.persistence.filter.test.domain.Country;
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

	@Before
	public void startDatabase() throws ScriptException, SQLException {
		if (!isDatabaseStarted) {
			isDatabaseStarted = true;
			ScriptUtils.executeSqlScript(dataSource.getConnection(), script);
		}
	}

	@Test
	public void between() {
		final float lesserVal = 35.0f;
		final float greaterVal = 50.0f;

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.between("lifeExpectancy", lesserVal, greaterVal));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getLifeExpectancy().floatValue() >= lesserVal);
			Assert.assertTrue(result.getLifeExpectancy().floatValue() <= greaterVal);
		}
	}

	@Test
	public void endsWith() {
		final String endsWithVal = "States";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.endsWith("name", endsWithVal));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getName().endsWith(endsWithVal));
		}
	}

	@Test
	public void equal() {
		final String equalVal = "USA";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.equal("code", equalVal));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertTrue(results.size() == 1);
		for (Country result : results) {
			Assert.assertTrue(result.getCode().equals(equalVal));
		}
	}

	@Test
	public void greaterThan() {
		final float greaterThanVal = 9970610.0f;

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.greaterThan("surfaceArea", greaterThanVal));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		Assert.assertTrue(results.size() == 2);
		for (Country result : results) {
			Assert.assertTrue(result.getSurfacearea() > greaterThanVal);
		}
	}

	@Test
	public void greaterThanOrEqual() {
		final float greaterThanVal = 9970610.0f;

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.greaterThanOrEqual("surfaceArea", greaterThanVal));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		Assert.assertTrue(results.size() == 3);
		for (Country result : results) {
			Assert.assertTrue(result.getSurfacearea() >= greaterThanVal);
		}
	}

	@Test
	public void iEndsWith() {
		final String insensitiveEndsWithVal = "STATES";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iEndsWith("name", insensitiveEndsWithVal));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getName().toUpperCase().endsWith(insensitiveEndsWithVal));
		}
	}

	@Test
	public void iEqual() {
		final String iEqualVal = "usa";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iEqual("code", iEqualVal));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertTrue(results.size() == 1);
		for (Country result : results) {
			Assert.assertTrue(result.getCode().toUpperCase().equals(iEqualVal.toUpperCase()));
		}
	}

	@Test
	public void iLike() {
		final String iLikeVal = "republic";

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iLike("name", iLikeVal));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getName().toUpperCase().contains(iLikeVal.toUpperCase()));
		}
	}

	@Test
	public void iLikeAny() {
		final String[] iLikeAnyVal = { "republic", "democratic" };

		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iLikeAny("name", iLikeAnyVal));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			System.out.println(result.getName());

			String nameUpper = result.getName().toUpperCase();
			boolean containsAny = false;
			for (String value : iLikeAnyVal) {
				containsAny |= nameUpper.contains(value.toUpperCase());
			}
			Assert.assertTrue(containsAny);
		}
	}

}