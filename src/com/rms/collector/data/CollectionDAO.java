package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.Card;
import com.rms.collector.model.Collection;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class CollectionDAO extends DAO implements iDAO<Collection> {

	@Override
	public List<Collection> findAll() {
		List<Collection> allCollections = new ArrayList<Collection>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Collection");

			// fetch all events from database
			Collection collection;
			
			while (rs.next()) {
				collection = new Collection();
				collection.setId(rs.getInt(1));
				collection.setName(rs.getString(2));
				collection.setUserId(rs.getInt(3));
				allCollections.add(collection);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allCollections;
	}
	
	public List<Collection> findByUserId(Integer userId) {
		List<Collection> allCollections = new ArrayList<Collection>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Collection WHERE user_id = " + userId.toString());

			// fetch all events from database
			Collection collection;
			
			while (rs.next()) {
				collection = new Collection();
				collection.setId(rs.getInt(1));
				collection.setName(rs.getString(2));
				collection.setUserId(rs.getInt(3));
				allCollections.add(collection);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allCollections;
	}
	
	public Collection findSingle(List<Filter> filters) {
		List<Collection> collections = find(filters);
		if (collections.size() > 0) {
			return collections.get(0);
		}
		return null;
	}
	
	public List<Collection> find(List<Filter> filters) {
		List<Collection> results = new ArrayList<Collection>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM Collection WHERE ", filters));

			// fetch all events from database
			Collection collection;
			
			while (rs.next()) {
				collection = new Collection();
				collection.setId(rs.getInt(1));
		        collection.setName(rs.getString(2));
				collection.setUserId(rs.getInt(3));
				results.add(collection);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return results;
	}

	@Override
	public boolean delete(Collection entity) throws SQLException {
		return execute("DELETE FROM Collection WHERE id = '" + entity.getId() + "'");
	}

	@Override
	public Object insert(Collection entity) throws SQLException {
		return executeReturn("INSERT INTO Collection(name,user_id) " +
                "VALUES ('" + Util.sqlFilter(entity.getName()) + "'," + entity.getUserId() + ")");
	}

	@Override
	public boolean update(Collection entity) throws SQLException {
		return execute("UPDATE Collection SET name = '" + Util.sqlFilter(entity.getName()) + 
                "', user_id = " + entity.getUserId() + 
                " where id = " + entity.getId());
	}

}
