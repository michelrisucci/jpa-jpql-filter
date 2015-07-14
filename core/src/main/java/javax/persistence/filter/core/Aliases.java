package javax.persistence.filter.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.filter.Filter;

/**
 * @author Michel Risucci
 */
public class Aliases {

	public static final String ROOT_PREFIX = "x";
	public static final String DOT = ".";
	public static final String DOT_REGEX = Pattern.quote(DOT);

	private LinkedHashMap<String, String> internal;

	/**
	 * 
	 */
	private Aliases(Filter<?> filter) {
		internal = new LinkedHashMap<String, String>();
	}

	/**
	 * @param filter
	 * @return
	 */
	public static StringBuilder process(Filter<?> filter) {
		Aliases aliases = new Aliases(filter);
		return aliases.register(filter);
	}

	private <E> StringBuilder register(Filter<E> filter) {
		StringBuilder joins = new StringBuilder();
		// Registering wheres aliases
		List<Where> wheres = filter.getWheres();
		if (wheres != null && !wheres.isEmpty()) {
			for (Where where : filter.getWheres()) {
				register(where, joins);
			}
		}
		// Registering orders aliases
		List<Order> orders = filter.getOrders();
		if (orders != null && !orders.isEmpty()) {
			for (Order order : filter.getOrders()) {
				register(order, joins);
			}
		}
		return joins;
	}

	/**
	 * @param path
	 * @param joins
	 * @return
	 */
	private LinkedHashMap<String, String> register(VolatilePath path,
			StringBuilder joins) {
		String fullPath = ROOT_PREFIX + DOT + path.getPath();
		String[] parts = fullPath.split(DOT_REGEX);
		if (parts.length > 1) {
			return register(path, parts, joins, 0);
		} else {
			return new LinkedHashMap<String, String>();
		}
	}

	/*
	 */
	private LinkedHashMap<String, String> register(VolatilePath path,
			String[] parts, StringBuilder joins, int i) {

		String part = parts[i];

		String currentPath;
		String currentAlias;

		if (i == 0) {
			currentPath = currentAlias = part;
			internal.put(currentPath, currentAlias);
			return register(path, parts, joins, ++i);
		} else if (i < parts.length - 1) {
			String lastPath = "";
			if (i == 1) {
				lastPath += parts[0];
			} else {
				for (int j = 0; j < i; j++) {
					lastPath += parts[j];
				}
			}

			currentPath = lastPath + DOT + part;
			currentAlias = lastPath + part;

			if (i == parts.length - 2) {
				path.setRealPath(currentAlias + DOT + parts[parts.length - 1]);
			}

			joins.append("INNER JOIN ") //
					.append(currentPath) //
					.append(" ") //
					.append(currentAlias) //
					.append(" ");

			internal.put(currentPath, currentAlias);
			return register(path, parts, joins, ++i);
		} else {
			return internal;
		}
	}

	/**
	 * @param path
	 * @return
	 */
	public String retrieve(String path) {
		String realPath = internal.get(path);
		return realPath == null ? path : realPath;
	}

}