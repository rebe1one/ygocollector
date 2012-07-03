package com.rms.collector.model.view;

public class Order {
	private String column;
	private String dir;
	public Order(String column, String dir) {
		this.column = column;
		this.dir = dir;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
}