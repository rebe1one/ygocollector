package com.rms.collector.util;

import java.util.LinkedList;

public class FilterList {
	private LinkedList<Filter> list;
	
	private FilterList(){
		list = new LinkedList<Filter>();
	}
	
	public static FilterList start(String id, Object value, Filter.Equality equality) {
		FilterList newList = new FilterList();
		newList.list.add(new Filter(id, value, equality));
		return newList;
	}
	
	public FilterList and(String id, Object value, Filter.Equality equality) {
		this.list.add(Filter.AND);
		this.list.add(new Filter(id, value, equality));
		return this;
	}
	
	public FilterList or(String id, Object value, Filter.Equality equality) {
		this.list.add(Filter.OR);
		this.list.add(new Filter(id, value, equality));
		return this;
	}
	public FilterList limit(int lim) {
		this.list.add(new LimitFilter("LIMIT", lim));
		return this;
	}
	
	public LinkedList<Filter> getList() {
		return this.list;
	}
}
