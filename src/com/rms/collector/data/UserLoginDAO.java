package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.User;
import com.rms.collector.model.UserLogin;

public class UserLoginDAO extends DAO implements iDAO<UserLogin> {
	protected final DataSource ds = DataSource.INSTANCE;

	@Override
	public List<UserLogin> findAll() {
		List<UserLogin> allUserLogins = new ArrayList<UserLogin>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserLogin");

			// fetch all events from database
			UserLogin userLogin;
			
			while (rs.next()) {
				userLogin = new UserLogin();
				userLogin.setUserId(rs.getInt(1));
				userLogin.setUserLogin(rs.getString(2));
				userLogin.setPassword(rs.getString(3));
				allUserLogins.add(userLogin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allUserLogins;
	}
	
	public UserLogin findByLogin(UserLogin userLogin) {
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserLogin WHERE user_login = '" 
					+ userLogin.getUserLogin() + "'");

			// fetch all events from database
			UserLogin result = new UserLogin();
			
			while (rs.next()) {
				result.setUserId(rs.getInt(1));
				result.setUserLogin(rs.getString(2));
				result.setPassword(rs.getString(3));
			}
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return new UserLogin();
		} finally {
		    ds.close();
		}
	}

	@Override
	public boolean delete(UserLogin entity) throws SQLException {
		return execute("DELETE FROM UserLogin WHERE user_id = '" + entity.getUserId() + "'");
	}

	@Override
	public Boolean insert(UserLogin entity) throws SQLException {
		return execute("INSERT INTO UserLogin(user_id,user_login,password) " +
                "VALUES (" + entity.getUserId() + ",'" + entity.getUserLogin() +
                "','" + entity.getPassword() + "')");
	}

	@Override
	public boolean update(UserLogin entity) throws SQLException {
		return execute("UPDATE UserLogin SET user_login = '" + entity.getUserLogin() + 
                "', password = '" + entity.getPassword() + "' where user_id = " + entity.getUserId());
	}

}
