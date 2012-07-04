package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.UserLoginView;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class UserLoginViewDAO extends DAO {
	protected final DataSource ds = DataSource.INSTANCE;

	public List<UserLoginView> findAll() {
		List<UserLoginView> allUsers = new ArrayList<UserLoginView>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserLoginView");

			// fetch all events from database
			UserLoginView user;
			
			while (rs.next()) {
				user = new UserLoginView();
				user.setId(rs.getInt(1));
				user.setFirstName(rs.getString(2));
				user.setLastName(rs.getString(3));
				user.setEmail(rs.getString(4));
				user.setUserLogin(rs.getString(5));
				allUsers.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allUsers;
	}
	
	public UserLoginView findSingle(List<Filter> filters) {
		List<UserLoginView> user = find(filters);
		if (user.size() > 0) {
			return user.get(0);
		}
		return null;
	}
	
	public List<UserLoginView> find(List<Filter> filters) {
		List<UserLoginView> users = new ArrayList<UserLoginView>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM UserLoginView WHERE ", filters));

			// fetch all events from database
			UserLoginView user;
			
			while (rs.next()) {
				user = new UserLoginView();
				user.setId(rs.getInt(1));
		        user.setFirstName(rs.getString(2));
				user.setLastName(rs.getString(3));
				user.setEmail(rs.getString(4));
				user.setUserLogin(rs.getString(5));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return users;
	}

}
