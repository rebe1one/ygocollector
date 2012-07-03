package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.Card;
import com.rms.collector.model.Rarity;
import com.rms.collector.model.Source;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class SourceDAO extends DAO implements iDAO<Source> {
	
	@Override
	public List<Source> findAll() {
		List<Source> allSources = new ArrayList<Source>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Source");

			Source source;
			
			while (rs.next()) {
				source = new Source();
				source.setId(rs.getInt(1));
		        source.setName(rs.getString(2));
				allSources.add(source);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allSources;
	}
	
	public Source findSingle(List<Filter> filters) {
		List<Source> source = find(filters);
		if (source.size() > 0) {
			return source.get(0);
		}
		return null;
	}
	
	public List<Source> find(List<Filter> filters) {
		List<Source> allSources = new ArrayList<Source>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM Source WHERE ", filters));

			Source source;
			
			while (rs.next()) {
				source = new Source();
				source.setId(rs.getInt(1));
		        source.setName(rs.getString(2));
				allSources.add(source);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		return allSources;
	}
	
	@Override
	public boolean delete(Source r) throws SQLException {
		return execute("DELETE FROM Source WHERE id = '" + r.getId() + "'");
	}
	
	@Override
	public Boolean insert(Source r) throws SQLException {
		return execute("INSERT INTO Source(id,name) " +
                    "VALUES ('" + r.getId() + "','" + r.getName() + "')");
	}
	
	@Override
	public boolean update(Source r) throws SQLException {
        return execute("UPDATE Source SET name = '" + r.getName() + 
                    "' where id = '" + r.getId() + "'");
    }

}