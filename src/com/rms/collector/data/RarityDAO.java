package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.Card;
import com.rms.collector.model.Rarity;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class RarityDAO extends DAO implements iDAO<Rarity> {
	
	@Override
	public List<Rarity> findAll() {
		List<Rarity> allRarities = new ArrayList<Rarity>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Rarity");

			Rarity rarity;
			
			while (rs.next()) {
				rarity = new Rarity();
				rarity.setRarity(rs.getString(1));
		        rarity.setDescription(rs.getString(2));
				allRarities.add(rarity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allRarities;
	}
	
	public Rarity findSingle(List<Filter> filters) {
		List<Rarity> rarity = find(filters);
		if (rarity.size() > 0) {
			return rarity.get(0);
		}
		return null;
	}
	
	public List<Rarity> find(List<Filter> filters) {
		List<Rarity> allRarities = new ArrayList<Rarity>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM Rarity WHERE ", filters));

			Rarity rarity;
			
			while (rs.next()) {
				rarity = new Rarity();
				rarity.setRarity(rs.getString(1));
		        rarity.setDescription(rs.getString(2));
				allRarities.add(rarity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		return allRarities;
	}
	
	@Override
	public boolean delete(Rarity r) throws SQLException {
		return execute("DELETE FROM Rarity WHERE rarity = '" + r.getRarity() + "'");
	}
	
	@Override
	public Boolean insert(Rarity r) throws SQLException {
		return execute("INSERT INTO Rarity(rarity,description) " +
                    "VALUES ('" + r.getRarity() + "','" + r.getDescription() + "')");
	}
	
	@Override
	public boolean update(Rarity r) throws SQLException {
        return execute("UPDATE Rarity SET description = '" + r.getDescription() + 
                    "' where rarity = '" + r.getRarity() + "'");
    }

}