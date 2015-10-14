package javax.persistence.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Abstract definition of an entity that must be versioned.</br>
 * Important: annotate your class with the default generator
 * <b>{@code @SequenceGenerator(name = "identity-sequence")}</b>. Changing all
 * other parameters is allowed.
 * 
 * @author Michel Risucci
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class VersionedIdentity<I extends Number> extends Identity<I> {

	@Version
	@Column(name = "version")
	private Long version;

	/**
	 * 
	 */
	public VersionedIdentity() {
		super();
	}

	/**
	 * @return last known modification for this entity. For each update of this
	 *         entity, this value is automatically incremented
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * Automatic value: you must not change this. JPA will automatically
	 * increment this entity version.
	 * 
	 * @param version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersionedIdentity<?> other = (VersionedIdentity<?>) obj;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}
