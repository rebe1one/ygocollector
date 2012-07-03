package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.Card;
import com.rms.collector.model.Collection;
import com.rms.collector.model.CollectionCard;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class CollectionCardDAO extends DAO implements iDAO<CollectionCard> {

	@Override
	public List<CollectionCard> findAll() {
		List<CollectionCard> allCollectionCards = new ArrayList<CollectionCard>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CollectionCard");

			// fetch all events from database
			CollectionCard cc;
			
			while (rs.next()) {
				cc = new CollectionCard();
				cc.setCollectionId(rs.getInt(1));
				cc.setCardId(rs.getInt(2));
				cc.setAmount(rs.getInt(3));
				cc.setRarity(rs.getString(4));
				cc.setLocationId(rs.getInt(5));
				cc.setPriceSourceId(rs.getInt(6));
				cc.setSetId(rs.getString(7));
				allCollectionCards.add(cc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allCollectionCards;
	}
	
	public CollectionCard findSingle(List<Filter> filters) {
		List<CollectionCard> ccs = find(filters);
		if (ccs.size() > 0) {
			return ccs.get(0);
		}
		return null;
	}
	
	public List<CollectionCard> find(List<Filter> filters) {
		List<CollectionCard> allCollectionCards = new ArrayList<CollectionCard>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM CollectionCard WHERE ", filters));

			CollectionCard cc;
			
			while (rs.next()) {
				cc = new CollectionCard();
				cc.setCollectionId(rs.getInt(1));
				cc.setCardId(rs.getInt(2));
				cc.setAmount(rs.getInt(3));
				cc.setRarity(rs.getString(4));
				cc.setLocationId(rs.getInt(5));
				cc.setPriceSourceId(rs.getInt(6));
				cc.setSetId(rs.getString(7));
				allCollectionCards.add(cc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allCollectionCards;
	}

	@Override
	public boolean delete(CollectionCard entity) throws SQLException {
		return execute("DELETE FROM CollectionCard WHERE collection_id = '" + entity.getCollectionId() 
				+ "' AND card_id = '" + entity.getCardId() + "' AND rarity = '" + entity.getRarity()
				+ "' AND location_id = '" + entity.getLocationId()
				+ "' AND price_source_id = '" + entity.getPriceSourceId()
				+ "' AND set_id = '" + Util.sqlFilter(entity.getSetId()) + "'");
	}

	@Override
	public Object insert(CollectionCard entity) throws SQLException {
		return execute("INSERT INTO CollectionCard(collection_id, card_id, amount, rarity, location_id, price_source_id, set_id) " +
                "VALUES ('" + entity.getCollectionId() + "','" + entity.getCardId() + "','" 
				+ entity.getAmount() + "', '" + entity.getRarity() + "', '" + entity.getLocationId() + "', '" + entity.getPriceSourceId() + "', '" + entity.getSetId()  + "')");
	}

	@Override
	public boolean update(CollectionCard entity) throws SQLException {
		return execute("UPDATE CollectionCard SET amount = '" + entity.getAmount() + 
                "' where collection_id = '" + entity.getCollectionId() + 
                "' and rarity = '" + entity.getRarity() +
                "' and location_id = '" + entity.getLocationId() +
                "' and card_id = " + entity.getCardId() +
				"' and price_source_id = " + entity.getPriceSourceId() + 
				"' and set_id = " + entity.getSetId());
	}

}
