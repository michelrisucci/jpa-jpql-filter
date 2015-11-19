package javax.persistence.filter.service;

import java.io.Serializable;

import javax.persistence.filter.repository.JpaFilterRepository;

public interface GenericFilterService<E, ID extends Serializable> extends FilterService<E, ID, JpaFilterRepository> {

}