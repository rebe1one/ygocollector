package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.rms.collector.model.UserEBayInfo;

public class UserEBayInfoDAO extends DAO {
	protected final DataSource ds = DataSource.INSTANCE;

	public UserEBayInfo findById(Integer id) {
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserEBayInfo WHERE user_id = '" 
					+ id+ "'");

			UserEBayInfo ebayInfo = null;
			
			while (rs.next()) {
				ebayInfo = new UserEBayInfo();
				ebayInfo.setUserId(rs.getInt(1));
				ebayInfo.setEBayToken(rs.getBlob(2).getBytes(1, (int)rs.getBlob(2).length()));
				ebayInfo.setEBayTokenExpirationDate(rs.getTimestamp(3));
			}
			
			return ebayInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			return new UserEBayInfo();
		} finally {
		    ds.close();
		}
	}

	public boolean delete(UserEBayInfo entity) throws SQLException {
		return execute("DELETE FROM UserEBayInfo WHERE user_id = '" + entity.getUserId() + "'");
	}

	public Boolean insert(UserEBayInfo entity) throws SQLException {
		return execute("INSERT INTO UserEBayInfo(user_id,ebay_token,ebay_token_expiration_date) " +
                "VALUES (" + entity.getUserId() + ",'" + entity.getEBayToken() +
                "','" + entity.getEBayTokenExpirationDate() + "')");
	}

	public boolean update(UserEBayInfo entity) throws SQLException {
		return execute("UPDATE UserEBayInfo SET ebay_token = '" + entity.getEBayToken() + 
                "', ebay_token_expiration_date = '" + entity.getEBayTokenExpirationDate() + "' where user_id = " + entity.getUserId());
	}

}
