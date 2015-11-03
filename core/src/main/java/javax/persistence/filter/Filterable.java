package javax.persistence.filter;

/**
 * An interface that defines a filterable component.
 * 
 * @author Michel Risucci
 */
public interface Filterable {

	/**
	 * Filters any defined entity with fetch range.
	 * 
	 * @param <T>
	 *            dynamic entity Java type
	 * @param filter
	 *            filled {@link Filter} model
	 * @param offset
	 *            SQL fetch offset
	 * @param limit
	 *            SQL fetch limit
	 * @return {@link PageFilter} object containing the filtered results
	 */
	<T> PageFilter<T> filter(Filter<T> filter, int offset, int limit);

	/**
	 * Filters any defined entity without fetch range.
	 * 
	 * @param <T>
	 *            dynamic entity Java type
	 * @param filter
	 *            filled {@link Filter} model
	 * @return {@link PageFilter} object containing the filtered results
	 */
	<T> PageFilter<T> filter(Filter<T> filter);

}