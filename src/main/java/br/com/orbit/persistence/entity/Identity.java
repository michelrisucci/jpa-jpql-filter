package br.com.orbit.persistence.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract definition of an entity that uses sequential ID.
 * 
 * @author Michel Risucci
 */
@MappedSuperclass
public abstract class Identity<I extends Comparable<I>> {

	private I id;

	/**
	 * 
	 */
	public Identity() {
		super();
	}

	/**
	 * @return sequential ID for this entity
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public I getId() {
		return id;
	}

	/**
	 * Automatic value: you must not change this. JPA will automatically
	 * increment this, sequentially.
	 * 
	 * @param id
	 */
	public void setId(I id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identity<?> other = (Identity<?>) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
