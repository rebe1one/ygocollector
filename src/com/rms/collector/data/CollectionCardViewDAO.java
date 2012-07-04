package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.util.FilterList;
import com.rms.collector.util.Util;

public class CollectionCardViewDAO extends DAO {
	public List<CollectionCardView> findByCollectionId(int id) {
		List<CollectionCardView> allCollectionCards = new ArrayList<CollectionCardView>();
		try {
			// get connection
			Statement stmt = ds.getStatement();
			ResultSet rs = stmt
					.executeQuery("select * from CollectionCardView "
							+ "where collection_id = " + id
							+ " group by card_id, rarity, price_source_id, set_id ");

			// fetch all events from database
			CollectionCardView ccv;

			while (rs.next()) {
				ccv = new CollectionCardView();
				ccv.setCollectionId(rs.getInt(1));
				ccv.setCardId(rs.getInt(2));
				ccv.setAmount(rs.getInt(3));
				ccv.setRarity(rs.getString(4));
				ccv.setPriceSourceId(rs.getInt(5));
				ccv.setSetId(rs.getString(6));
				ccv.setName(rs.getString(7));
				ccv.setAttribute(rs.getString(8));
				ccv.setLocationId(rs.getInt(9));
				ccv.setLocationName(rs.getString(10));
				ccv.setImageFileName(rs.getString(11));
				ccv.setPrice(rs.getBigDecimal(12));
				ccv.setPriceDate(rs.getTimestamp(13));
				ccv.setUserId(rs.getString(14));
				allCollectionCards.add(ccv);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ds.close();
		}

		return allCollectionCards;
	}
	
	public List<CollectionCardView> find(FilterList filters) {
		List<CollectionCardView> allCollectionCards = new ArrayList<CollectionCardView>();
		try {
			// get connection
			Statement stmt = ds.getStatement();
			ResultSet rs = stmt
					.executeQuery("select * from CollectionCardView "
							+ "where " + Util.filterToSQL(filters.getList())
							+ " group by card_id, rarity, price_source_id, set_id ");

			// fetch all events from database
			CollectionCardView ccv;

			while (rs.next()) {
				ccv = new CollectionCardView();
				ccv.setCollectionId(rs.getInt(1));
				ccv.setCardId(rs.getInt(2));
				ccv.setAmount(rs.getInt(3));
				ccv.setRarity(rs.getString(4));
				ccv.setPriceSourceId(rs.getInt(5));
				ccv.setSetId(rs.getString(6));
				ccv.setName(rs.getString(7));
				ccv.setAttribute(rs.getString(8));
				ccv.setLocationId(rs.getInt(9));
				ccv.setLocationName(rs.getString(10));
				ccv.setImageFileName(rs.getString(11));
				ccv.setPrice(rs.getBigDecimal(12));
				ccv.setPriceDate(rs.getTimestamp(13));
				ccv.setUserId(rs.getString(14));
				allCollectionCards.add(ccv);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ds.close();
		}

		return allCollectionCards;
	}
}
