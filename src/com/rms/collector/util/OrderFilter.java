package com.rms.collector.util;

public class OrderFilter extends Filter {
	public static String ASC = "ASC";
	public static String DESC = "DESC";
	public OrderFilter(String key, String direction) {
		super(key, direction);
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(" ORDER BY ");
		b.append(key);
		b.append(" ");
		b.append(value);
		return b.toString();
	}
}
