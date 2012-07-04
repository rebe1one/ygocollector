package com.rms.collector.model.view;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.rms.collector.data.ViewDAO;
import com.rms.collector.util.Util;

public class View {
	private String name;
	private List<Table> tables;
	private Order orderBy;
	
	public View(String name) {
		this.name = name;
		this.tables = new LinkedList<Table>();
	}
	
	public void addTable(Table table) {
		tables.add(table);
	}
	
	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void create() throws SQLException {
		ViewDAO dao = new ViewDAO();
		StringBuilder b = new StringBuilder();
		b.append("CREATE VIEW ");
		b.append(this.name);
		b.append(" AS SELECT ");
		boolean cf = false;
		for (Table t : tables) {
			if (Util.isNotEmpty(t.getColumns())) {
				for (Column c : t.getColumns()) {
					if (cf) b.append(", ");
					else cf = true;
					b.append(Util.isNotEmpty(t.getAlias()) ? t.getAlias() : t.getName());
					b.append(".");
					b.append(c.getName());
					if (Util.isNotEmpty(c.getAs())) {
						b.append(" AS ");
						b.append(c.getAs());
					}
				}
			} else {
				if (cf) b.append(", ");
				else cf = true;
				b.append(Util.isNotEmpty(t.getAlias()) ? t.getAlias() : t.getName());
				b.append(".* ");
			}
		}
		b.append(" FROM ");
		for (int i = 0; i < tables.size(); i++) {
			Table t = tables.get(i);
			if (i == 0) {
				b.append(t.getName());
				if (Util.isNotEmpty(t.getAlias())) {
					b.append(" AS ");
					b.append(t.getAlias());
				}
			} else {
				// TODO: different join types
				b.append(" LEFT OUTER JOIN ");
				if (Util.isNotEmpty(t.getOrder())) {
					StringBuilder sub = new StringBuilder();
					sub.append("CREATE VIEW ");
					sub.append(t.getAlias());
					sub.append(" AS SELECT * FROM ");
					sub.append(t.getName());
					sub.append(" ORDER BY ");
					sub.append(t.getOrder().getColumn());
					sub.append(" ");
					sub.append(t.getOrder().getDir());
					
					dao.run(dropView(t.getAlias()));
					dao.run(sub.toString());
					
					b.append(t.getAlias());
				} else {
					b.append(t.getName());
					if (Util.isNotEmpty(t.getAlias())) {
						b.append(" AS ");
						b.append(t.getAlias());
					}
				}
				b.append(" ON ");
				cf = false;
				for (Join j : t.getJoins()) {
					if (cf) b.append(" AND ");
					else cf = true;
					b.append(Util.isNotEmpty(t.getAlias()) ? t.getAlias() : t.getName());
					b.append(".");
					b.append(j.getFrom());
					b.append(" = ");
					if (j.isMax()) {
						b.append("(SELECT MAX(");
						b.append(j.getFrom());
						b.append(") FROM ");
						b.append(t.getName());
						b.append(" ");
						b.append(t.getName());
						b.append("_MAX WHERE ");
						boolean cf2 = false;
						for (Join j2 : t.getJoins()) {
							if (!j2.getFrom().equals(j.getFrom())) {
								if (cf2) b.append(" AND ");
								else cf2 = true;
								b.append(t.getName());
								b.append("_MAX.");
								b.append(j2.getFrom());
								b.append(" = ");
								b.append(j2.getOn());
								b.append(".");
								b.append(j2.getTo());
							}
						}
						b.append(")");
					} else {
						b.append(j.getOn());
						b.append(".");
						b.append(j.getTo());
					}
				}
			}
			
			if (t.getLimit() > 0) {
				b.append(" LIMIT ");
				b.append(t.getLimit());
			}
		}
		if (Util.isNotEmpty(orderBy)) {
			b.append(" ORDER BY ");
			b.append(orderBy.getColumn());
			b.append(" ");
			b.append(orderBy.getDir());
		}
		dao.run(dropView(this.name));
		dao.run(b.toString());
	}
	
	private String dropView(String name) {
		return "DROP VIEW IF EXISTS " + name;
	}

	public Order getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Order orderBy) {
		this.orderBy = orderBy;
	}
}
