package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.Price;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class PriceDAO extends DAO implements iDAO<Price> {
	
	@Override
	public List<Price> findAll() {
		List<Price> allPrices = new ArrayList<Price>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Price");

			Price price;
			
			while (rs.next()) {
				price = new Price();
				price.setCardId(rs.getInt(1));
		        price.setSourceId(rs.getInt(2));
				price.setEdition(rs.getString(3));
				price.setPrice(rs.getBigDecimal(4));
				price.setDate(rs.getTimestamp(5));
				price.setRarity(rs.getString(6));
				price.setSetId(rs.getString(7));
				allPrices.add(price);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allPrices;
	}
	
	public List<Price> find(List<Filter> filters) {
		List<Price> allPrices = new ArrayList<Price>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM Price WHERE ", filters));

			// fetch all events from database
			Price price;
			
			while (rs.next()) {
				price = new Price();
				price.setCardId(rs.getInt(1));
		        price.setSourceId(rs.getInt(2));
				price.setEdition(rs.getString(3));
				price.setPrice(rs.getBigDecimal(4));
				price.setDate(rs.getTimestamp(5));
				price.setRarity(rs.getString(6));
				price.setSetId(rs.getString(7));
				allPrices.add(price);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allPrices;
	}
	
	@Override
	public boolean delete(Price p) throws SQLException {
		return execute("DELETE FROM Price WHERE card_id = '" + p.getCardId() + "'");
	}
	
	@Override
	public Boolean insert(Price p) throws SQLException {
		return execute("INSERT INTO Price(card_id,source_id,edition,price,date,rarity,set_id) " +
                    "VALUES ('" + p.getCardId() + "','" + p.getSourceId() +
                    "','" + p.getEdition() + "','" + p.getPrice() + 
                    "','" + p.getDate() + "','" + p.getRarity() + "','" + p.getSetId() + "')");
	}
	
	@Override
	public boolean update(Price p) throws SQLException {
        return execute("UPDATE Card SET edition = '" + p.getEdition() + 
                    "' where card_id = " + p.getCardId() + " AND source_id = " + p.getSourceId() + 
                    " AND date = " + p.getDate() + " AND rarity = '" + p.getRarity() + "'");
    }

}