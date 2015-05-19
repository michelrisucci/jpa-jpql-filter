package javax.persistence.filter;

import java.util.List;

/**
 * Page filter object to hold filter list and count.
 * 
 * @author Michel Risucci
 *
 * @param <E>
 */
public class PageFilter<E> {

	private List<E> list;
	private long count;
	private int pageSize;
	private int numberOfPages;

	/**
	 * @param list
	 * @param pageSize
	 * @param count
	 */
	public PageFilter(List<E> list, int pageSize, long count) {
		super();
		this.list = list;
		this.pageSize = pageSize;
		this.count = count;
		this.numberOfPages = calcNumberOfPages();
	}

	/**
	 * @return
	 */
	private int calcNumberOfPages() {
		return calcNumberOfPages(pageSize, count);
	}

	/**
	 * @param pageSize
	 * @param count
	 * @return
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
	 * @return
	 */
	public List<E> getList() {
		return list;
	}

	/**
	 * @return
	 */
	public long getCount() {
		return count;
	}

	/**
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return
	 */
	public int getNumberOfPages() {
		return numberOfPages;
	}

}