package com.rms.collector.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public enum DataSource {
	
	INSTANCE;
	
	private static final String url = "jdbc:mysql://www.andreitulai.com/andreit1_ygo";
	private static final String user = "andreit1_ygo";
	private static final String pwd = "ygoADMIN";

	private Connection conn = null;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private DataSource() {

	}

	public Statement getStatement() throws SQLException {
		Statement stmt = null;
		// get connection
		conn = DriverManager.getConnection(url, user, pwd);
		stmt = conn.createStatement();
		return stmt;
	}

	public void close() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}