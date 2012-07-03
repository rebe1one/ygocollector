package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO {
	protected final DataSource ds = DataSource.INSTANCE;
	
	protected boolean execute(String sql) throws SQLException {
		System.out.println(sql);
		Statement stmt = null;
		try {
			stmt = ds.getStatement();
			stmt.execute(sql);
			return true;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			ds.close();
		}
	}

	protected int executeReturn(String sql) throws SQLException {
		System.out.println(sql);
		Statement stmt = null;
		try {
			stmt = ds.getStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			int result = 0;
			if (rs.next()) {
				result = rs.getInt(1);
			}
			return result;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			ds.close();
		}
	}

	public void startTransaction() {
		try {
			this.execute("START TRANSACTION");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void commmitTransaction() {
		try {
			this.execute("COMMIT");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void rollbackTransaction() {
		try {
			this.execute("ROLLBACK");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
