package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO {
	protected final DataSource ds = DataSource.INSTANCE;
	
	protected boolean execute(String sql) throws SQLException {
		System.out.println(sql);
		try {
			Statement stmt = ds.getStatement();
			stmt.execute(sql);
			if (stmt != null) {
				stmt.close();
			}

			return true;
		} finally {
			ds.close();
		}
	}

	protected int executeReturn(String sql) throws SQLException {
		System.out.println(sql);
		try {
			Statement stmt = ds.getStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			int result = 0;
			if (rs.next()) {
				result = rs.getInt(1);
			}
			if (stmt != null) {
				stmt.close();
			}
			return result;
		} finally {
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
