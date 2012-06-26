package com.rms.collector.util;

public class LimitFilter extends Filter {

	public LimitFilter(String key, Object value) {
		super(key, value);
	}
	
	@Override
	public String toString() {
		return " LIMIT " + value;
	}
}
