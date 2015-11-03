package javax.persistence.filter;

import java.util.List;

/**
 * Page filter object to hold filter list and count.
 * 
 * @author Michel Risucci
 *
 * @param <E>
 *            entity Java Class type
 */
public class PageFilter<E> {

	private List<E> list;
	private long count;
	private int pageSize;
	private int numberOfPages;

	/**
	 * Mandatory constructor.
	 * 
	 * @param list
	 *            the fetched objects itself
	 * @param pageSize
	 *            the pagination portion
	 * @param count
	 *            number of results for the defined {@link Filter}
	 */
	public PageFilter(List<E> list, int pageSize, long count) {
		super();
		this.list = list;
		this.pageSize = pageSize;
		this.count = count;
		this.numberOfPages = calcNumberOfPages();
	}

	/**
	 * Calculates the number of pages, according to count and page size.
	 * 
	 * @return number of pages according to count and page size
	 */
	private int calcNumberOfPages() {
		return calcNumberOfPages(pageSize, count);
	}

	/**
	 * Calculates the number of pages, according to count and page size.
	 * 
	 * @param pageSize
	 *            pagination size
	 * @param count
	 *            fetch count from database
	 * @return number of pages according to count and page size
	 */
	private static int calcNumberOfPages(int pageSize, long count) {
		if (count == 0 || pageSize == 0) {
			return 0;
		}

		double doublePageSize = (double) pageSize;
		double doubleCount = (double) count;
		double doubleRate = doubleCount / doublePageSize;
		int intRate = (int) doubleRate;

		if (doubleRate / intRate == 1d) {
			return intRate;
		} else {
			return intRate + 1;
		}
	}

	/**
	 * @return wrapped list of fetched entities
	 */
	public List<E> getList() {
		return list;
	}

	/**
	 * @return wrapped count of entities
	 */
	public long getCount() {
		return count;
	}

	/**
	 * @return wrapped page size
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return wrapped number of pages according to count and page size
	 */
	public int getNumberOfPages() {
		return numberOfPages;
	}

}