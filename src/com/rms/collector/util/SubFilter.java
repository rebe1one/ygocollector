package com.rms.collector.util;

public class SubFilter extends Filter {
	
	private String table;
	private String selectColumn;

	public String getSelectColumn() {
		return selectColumn;
	}

	public void setSelectColumn(String selectColumn) {
		this.selectColumn = selectColumn;
	}

	public SubFilter(String table, String key, Object value) {
		super(key, value);
		this.table = table;
	}
	
	public SubFilter(String table, String key, Object value, Filter.Equality equality) {
		super(key, value, equality);
		this.table = table;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("(SELECT ");
		if (Util.isNotEmpty(selectColumn)) b.append(selectColumn);
		else b.append("*");
		b.append(" FROM ");
		b.append(table);
		b.append(" WHERE ");
		b.append(this.key);
		b.append(" = '");
		b.append(this.value);
		b.append("')");
		return b.toString();
	}
}
