package com.rms.collector.util;

import java.util.LinkedList;
import java.util.List;

public class Filter {
	protected String key;
	protected Object value;
	protected boolean notEqual;
	
	public static Filter AND = new Filter("AND", "AND");
	public static Filter OR = new Filter("OR", "OR");
	public static Filter LB = new Filter("LB", "LB");
	public static Filter RB = new Filter("RB", "RB");
	
	public Filter(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public Filter(String key, Object value, boolean notEqual) {
		this.key = key;
		this.value = value;
		this.notEqual = notEqual;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public static List<Filter> begin() {
		return new LinkedList<Filter>();
	}
	
	public static List<Filter> simpleFilter(String key, Object value) {
		List<Filter> fl = new LinkedList<Filter>();
		fl.add(new Filter(key, value));
		return fl;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(key);
		if (notEqual) b.append(" <> '");
		else b.append(" = '");
		b.append(value.toString().replace("'", "''"));
		b.append("'");
		return b.toString();
	}
}
