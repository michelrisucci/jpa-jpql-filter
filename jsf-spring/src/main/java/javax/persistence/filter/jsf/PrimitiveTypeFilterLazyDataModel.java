package javax.persistence.filter.jsf;

import java.util.List;
import java.util.Map;

import javax.persistence.filter.PageFilter;
import javax.persistence.filter.service.FilterService;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

public class PrimitiveTypeFilterLazyDataModel<E, B extends FilterBean<E, FilterService<E, ?, ?>>> extends LazyDataModel<E> {

	private static final long serialVersionUID = 1734399832369799534L;

	private B bean;

	public PrimitiveTypeFilterLazyDataModel(B bean) {
		this.bean = bean;
	}

	@Override
	public List<E> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		int currentPage = first > pageSize ? first % pageSize : 1;
		bean.setCurrentPage(currentPage);
		bean.setPageSize(pageSize);

		PageFilter<E> pageFilter = bean.doFilter(currentPage, pageSize);
		setRowCount(Long.valueOf(pageFilter.getCount()).intValue());

		return pageFilter.getList();
	}

}