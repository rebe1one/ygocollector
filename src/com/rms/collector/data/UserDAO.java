package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.Card;
import com.rms.collector.model.User;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class UserDAO extends DAO implements iDAO<User> {
	protected final DataSource ds = DataSource.INSTANCE;

	@Override
	public List<User> findAll() {
		List<User> allUsers = new ArrayList<User>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM User");

			// fetch all events from database
			User user;
			
			while (rs.next()) {
				user = new User();
				user.setId(rs.getInt(1));
				user.setFirstName(rs.getString(2));
				user.setLastName(rs.getString(3));
				user.setEmail(rs.getString(4));
				allUsers.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allUsers;
	}
	
	public User findSingle(List<Filter> filters) {
		List<User> user = find(filters);
		if (user.size() > 0) {
			return user.get(0);
		}
		return null;
	}
	
	public List<User> find(List<Filter> filters) {
		List<User> users = new ArrayList<User>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM User WHERE ", filters));

			// fetch all events from database
			User user;
			
			while (rs.next()) {
				user = new User();
				user.setId(rs.getInt(1));
		        user.setFirstName(rs.getString(2));
				user.setLastName(rs.getString(3));
				user.setEmail(rs.getString(4));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return users;
	}

	@Override
	public boolean delete(User entity) throws SQLException {
		return execute("DELETE FROM User WHERE id = '" + entity.getId() + "'");
	}

	@Override
	public Integer insert(User entity) throws SQLException {
		return executeReturn("INSERT INTO User(first_name,last_name,email) " +
                "VALUES ('" + entity.getFirstName() + "','" + entity.getLastName() +
                "','" + entity.getEmail() + "')");
	}

	@Override
	public boolean update(User entity) throws SQLException {
		return execute("UPDATE User SET first_name = '" + entity.getFirstName() + 
                "', last_name = '" + entity.getLastName() + 
                "', email = " + entity.getEmail() + "' where id = '" + entity.getId() + "'");
	}

}
