package javax.persistence.filter.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

	public Aliases() {
		internal = new LinkedHashMap<String, String>();
	}

	public <E> StringBuilder register(Filter<E> filter) {
		StringBuilder joins = new StringBuilder();
		// Registering wheres aliases
		for (Where where : filter.getWheres()) {
			register(where, joins);
		}
		// Registering orders aliases
		for (Order order : filter.getOrders()) {
			register(order, joins);
		}
		return joins;
	}

	public void register(VolatilePath path, StringBuilder joins) {
		String fullPath = ROOT_PREFIX + DOT + path.getPath();
		String[] parts = fullPath.split(DOT_REGEX);
		if (parts.length > 1) {
			register(parts, joins, 0);
		}
	}

	public static void main(String[] args) {
		Aliases aliases = new Aliases();
		Where where = Where.equal("marido.filhos.animais.nome", "Pluto");
		StringBuilder builder = new StringBuilder();
		aliases.register(where, builder);

		System.out.println(builder.toString());
	}

	/*
	 * TODO FIX MUITO CONFUSO!!!
	 */
	private void register(String[] parts, StringBuilder joins, int i) {

		String part = parts[i];

		String lastPath;
		String currentPath;
		String currentAlias;

		if (i == 0) {
			currentPath = currentAlias = part;

			internal.put(currentPath, currentAlias);
			register(parts, joins, ++i);
		} else if (i < parts.length - 1) {
			if (i == 1) {
				lastPath = parts[0];
			} else {
				lastPath = "";
				for (int j = 0; j < i; j++) {
					lastPath += parts[j];
				}
			}

			currentPath = lastPath + DOT + part;
			currentAlias = lastPath + part;

			joins.append("INNER JOIN ") //
					.append(currentPath) //
					.append(" ") //
					.append(currentAlias) //
					.append(" ");

			internal.put(currentPath, currentAlias);
			register(parts, joins, ++i);
		}
	}

	public void registerJoins(StringBuilder jpql) {
		Collection<String> values = internal.values();
		for (Iterator<String> i = values.iterator(); i.hasNext();) {
			// TODO
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