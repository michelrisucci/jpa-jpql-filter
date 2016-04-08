package javax.persistence.filter.test;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.filter.Filter;
import javax.persistence.filter.core.Order;
import javax.persistence.filter.core.Where;
import javax.persistence.filter.test.context.Database;
import javax.persistence.filter.test.context.Hibernate;
import javax.persistence.filter.test.context.Jpa;
import javax.persistence.filter.test.domain.Country;
import javax.persistence.filter.test.service.CountryService;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@ComponentScan(basePackages = "javax")
@EnableTransactionManagement
public class JpaJpqlFilterTests {

	private static final Log log = LogFactory.getLog(JpaJpqlFilterTests.class);

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
		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.between("lifeExpectancy", 35.0f, 50.0f));
		filter.add(Order.byAscending("lifeExpectancy"));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getLifeExpectancy().floatValue() >= 35.0f);
			Assert.assertTrue(result.getLifeExpectancy().floatValue() <= 50.0f);
		}
		log.info("Between succeed!");
	}

	@Test
	public void endsWith() {
		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.endsWith("name", "States"));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getName().endsWith("States"));
		}
		log.info("EndsWith succeed!");
	}

	@Test
	public void equal() {
		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.equal("code", "USA"));
		filter.add(Order.byAscending("code"));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertTrue(results.size() == 1);
		for (Country result : results) {
			Assert.assertTrue(result.getCode().equals("USA"));
		}
		log.info("Equals succeed!");
	}

	@Test
	public void insensitiveEndsWith() {
		Filter<Country> filter = Filter.newInstance(Country.class);
		filter.add(Where.iEndsWith("name", "STATES"));

		List<Country> results = countryService.filter(filter).getList();
		Assert.assertFalse(results.isEmpty());
		for (Country result : results) {
			Assert.assertTrue(result.getName().toUpperCase().endsWith("STATES"));
		}
		log.info("InsensitiveEndsWith succeed!");
	}

}