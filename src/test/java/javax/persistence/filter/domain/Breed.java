package javax.persistence.filter.domain;

import javax.persistence.Entity;

@Entity
public class Breed extends Identity<Long> {

	public enum Size {
		TINY, SMALL, MEDIUM, BIG, GIANT;
	}

	private String name;
	private Size size;

	public Breed() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

}