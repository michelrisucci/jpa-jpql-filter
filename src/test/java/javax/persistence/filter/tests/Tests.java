package javax.persistence.filter.tests;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.filter.Filter;
import javax.persistence.filter.FilterService;
import javax.persistence.filter.Where;
import javax.persistence.filter.domain.Breed;
import javax.persistence.filter.domain.Breed.Size;
import javax.persistence.filter.domain.Dog;
import javax.persistence.filter.domain.Person;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests {

	public static EntityManager em;

	@BeforeClass
	public static void beforeClass() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("default");
		em = emf.createEntityManager();
	}

	@Test
	@SuppressWarnings("unused")
	public void test() {
		em.getTransaction().begin();
		Map<String, Breed> breeds = addBreeds();
		Map<String, Dog> dogs = addDogs(breeds);
		Map<String, Person> persons = addPersons(dogs);
		em.getTransaction().commit();

		Filter<Person> filter = Filter.newInstance(Person.class);
		filter.add(Where.insensitiveLike("dog.breed.name", "kit"));

		Person person = FilterService.list(em, filter, 0, 100).get(0);
		Assert.assertEquals(person.getName(), "Alberto");
	}

	/**
	 * @return
	 */
	public Map<String, Breed> addBreeds() {
		Map<String, Breed> breeds = new HashMap<String, Breed>();

		Breed breed;

		breed = new Breed();
		breed.setName("Akita");
		breed.setSize(Size.MEDIUM);
		em.persist(breed);
		breeds.put(breed.getName(), breed);

		breed = new Breed();
		breed.setName("Pinscher");
		breed.setSize(Size.TINY);
		em.persist(breed);
		breeds.put(breed.getName(), breed);

		return breeds;
	}

	/**
	 * @param breeds
	 * @return
	 */
	public Map<String, Dog> addDogs(Map<String, Breed> breeds) {
		Map<String, Dog> dogs = new HashMap<String, Dog>();

		Dog dog;

		dog = new Dog();
		dog.setName("Biscoito");
		dog.setBirthDate(newDate(23, Calendar.DECEMBER, 1994));
		dog.setBreed(breeds.get("Pinscher"));
		em.persist(dog);
		dogs.put(dog.getName(), dog);

		dog = new Dog();
		dog.setName("Pongo");
		dog.setBirthDate(newDate(18, Calendar.FEBRUARY, 2002));
		dog.setBreed(breeds.get("Akita"));
		em.persist(dog);
		dogs.put(dog.getName(), dog);

		dog = new Dog();
		dog.setName("Totó");
		dog.setBirthDate(newDate(3, Calendar.MARCH, 2008));
		dog.setBreed(breeds.get("Pinscher"));
		em.persist(dog);
		dogs.put(dog.getName(), dog);

		return dogs;
	}

	/**
	 * @param dogs
	 * @return
	 */
	public Map<String, Person> addPersons(Map<String, Dog> dogs) {
		Map<String, Person> persons = new HashMap<String, Person>();

		Person person;

		person = new Person();
		person.setName("Margareth");
		person.setDog(dogs.get("Biscoito"));
		person.setBirthDate(newDate(22, Calendar.OCTOBER, 1961));
		em.persist(person);
		persons.put(person.getName(), person);

		person = new Person();
		person.setName("Alberto");
		person.setDog(dogs.get("Pongo"));
		person.setBirthDate(newDate(16, Calendar.AUGUST, 1983));
		em.persist(person);
		persons.put(person.getName(), person);

		person = new Person();
		person.setName("Michel");
		person.setDog(dogs.get("Totó"));
		person.setBirthDate(newDate(18, Calendar.JANUARY, 1989));
		em.persist(person);
		persons.put(person.getName(), person);

		return persons;
	}

	/**
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public Date newDate(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}

}