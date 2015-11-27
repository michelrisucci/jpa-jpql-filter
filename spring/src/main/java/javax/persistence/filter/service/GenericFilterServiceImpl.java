package javax.persistence.filter.service;

import java.io.Serializable;

import javax.persistence.filter.repository.JpaFilterRepository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED)
public abstract class GenericFilterServiceImpl<E, ID extends Serializable> extends FilterServiceImpl<E, ID, JpaFilterRepository> implements GenericFilterService<E, ID> {
}