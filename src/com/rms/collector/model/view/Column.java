package com.rms.collector.model.view;

public class Column {
	private String name;
	private String as;
	public Column(String name, String as) {
		this.name = name;
		this.as = as;
	}
	public Column(String name) {
		this.name = name;
		this.as = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAs() {
		return as;
	}
	public void setAs(String as) {
		this.as = as;
	}
}