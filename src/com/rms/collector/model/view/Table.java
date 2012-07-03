package com.rms.collector.model.view;

import java.util.LinkedList;
import java.util.List;

public class Table {
	private String name;
	private String alias;
	private List<Join> joins;
	private List<Column> columns;
	private Order order;
	public Table(String name, String alias) {
		this.name = name;
		this.alias = alias;
		this.joins = new LinkedList<Join>();
		this.columns = new LinkedList<Column>();
	}
	public void addColumn(Column column) {
		columns.add(column);
	}
	public void addJoin(Join join) {
		joins.add(join);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public List<Join> getJoins() {
		return joins;
	}
	public void setJoins(List<Join> joins) {
		this.joins = joins;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}
