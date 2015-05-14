package javax.persistence.filter.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Identity<X extends Comparable<? super X>> {

	private X id;

	public Identity() {
	}

	@Id
	@GeneratedValue
	public X getId() {
		return id;
	}

	public void setId(X id) {
		this.id = id;
	}

}
