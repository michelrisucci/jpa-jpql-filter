package javax.persistence.filter.jsf;

import javax.persistence.filter.service.FilterService;

public abstract class LazyDataModelFilterBean<E, S extends FilterService<E, ?, ?>> extends FilterBean<E, S> {

	public LazyDataModelFilterBean() {
		super();
	}

}