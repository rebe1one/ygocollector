package com.rms.collector.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Util {
	public static boolean isEmpty(String s) {
		if (s == null || s.length() == 0) {
			return true;
		}
		return false;
	}
	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}
	public static boolean isEmpty(Object o) {
		return o == null ? true : false;
	}
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}
	
	public static Timestamp getCurrentTimestamp() {
		Date date = new Date();
		return new Timestamp(date.getTime());
	}
	
	public static String filterToSQL(List<Filter> filter) {
		StringBuilder b = new StringBuilder();
		String orderBy = null;
		for (Filter entry : filter) {
			if (entry.equals(Filter.AND)) {
				b.append(" AND ");
				continue;
			} else if (entry.equals(Filter.OR)) {
				b.append(" OR ");
				continue;
			} else if (entry.equals(Filter.LB)) {
				b.append(" (");
				continue;
			} else if (entry.equals(Filter.RB)) {
				b.append(") ");
				continue;
			} else if (entry.getClass().isInstance(OrderFilter.class)) {
				orderBy = entry.toString();
				continue;
			}
			b.append(entry.toString());
		}
		if (orderBy != null) b.append(orderBy);
		return b.toString();
	}
	
	public static String buildSQLString(String query, List<Filter> filters) {
		String sqlFilters = Util.filterToSQL(filters);
		if (isNotEmpty(sqlFilters)) {
			return new StringBuilder(query).append(sqlFilters).toString();
		}
		System.out.println(query);
		return query;
	}
	
	public static String sqlFilter(String in) {
		return in.replace("'", "''");
	}
	
}
