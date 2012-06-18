package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.Card;
import com.rms.collector.model.Collection;
import com.rms.collector.model.Location;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class LocationDAO extends DAO implements iDAO<Location> {

	@Override
	public List<Location> findAll() {
		List<Location> allLocations = new ArrayList<Location>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Location");

			// fetch all events from database
			Location location;
			
			while (rs.next()) {
				location = new Location();
				location.setId(rs.getInt(1));
				location.setUserId(rs.getInt(2));
				location.setName(rs.getString(3));
				allLocations.add(location);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allLocations;
	}
	
	public List<Location> findByUserId(Integer userId) {
		List<Location> allLocations = new ArrayList<Location>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Location WHERE user_id = " + userId.toString() + " ORDER BY id");

			Location location;
			
			while (rs.next()) {
				location = new Location();
				location.setId(rs.getInt(1));
				location.setUserId(rs.getInt(2));
				location.setName(rs.getString(3));
				allLocations.add(location);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allLocations;
	}
	
	public Location findSingle(List<Filter> filters) {
		List<Location> entities = find(filters);
		if (entities.size() > 0) {
			return entities.get(0);
		}
		return null;
	}
	
	public List<Location> find(List<Filter> filters) {
		List<Location> results = new ArrayList<Location>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM Location WHERE ", filters));

			Location location;
			
			while (rs.next()) {
				location = new Location();
				location.setId(rs.getInt(1));
				location.setUserId(rs.getInt(2));
				location.setName(rs.getString(3));
				results.add(location);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return results;
	}

	@Override
	public boolean delete(Location entity) throws SQLException {
		return execute("DELETE FROM Location WHERE id = '" + entity.getId() + "'");
	}

	@Override
	public Object insert(Location entity) throws SQLException {
		return execute("INSERT INTO Location(user_id,name) " +
                "VALUES ('" + entity.getUserId() + "','" + entity.getName() + "')");
	}

	@Override
	public boolean update(Location entity) throws SQLException {
		return execute("UPDATE Location SET name = '" + entity.getName() + 
                "', user_id = " + entity.getUserId() + 
                " where id = " + entity.getId());
	}

}
