package com.rms.collector.model.view;

public class Column {
	private String name;
	private String as;
	private boolean max;
	public Column(String name, String as) {
		this.name = name;
		this.as = as;
	}
	public Column(String name) {
		this.name = name;
		this.as = name;
	}
	public Column(String name, String as, boolean max) {
		this.name = name;
		this.as = as;
		this.setMax(max);
	}
	public Column(String name, boolean max) {
		this.name = name;
		this.as = name;
		this.setMax(max);
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
	public boolean isMax() {
		return max;
	}
	public void setMax(boolean max) {
		this.max = max;
	}
}