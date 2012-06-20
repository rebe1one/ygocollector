package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.view.CollectionCardView;

public class CollectionCardViewDAO extends DAO {
	public List<CollectionCardView> findByCollectionId(int id) {
		List<CollectionCardView> allCollectionCards = new ArrayList<CollectionCardView>();
		try {
			// get connection
			Statement stmt = ds.getStatement();
			ResultSet rs = stmt
					.executeQuery("select "
							+ "CollectionCard.card_id, "
							+ "CollectionCard.amount, "
							+ "CollectionCard.rarity, "
							+ "Card.name, "
							+ "Card.attribute, "
							+ "OrderedPrice.price, "
							+ "Location.id as location_id, "
							+ "Location.name as location_name, "
							+ "OrderedPrice.source_id, "
							+ "OrderedPrice.set_id "
							+ "from CollectionCard "
							+ "left outer join Card "
							+ "on CollectionCard.card_id = Card.id "
							+ "left outer join (select * from Price order by date desc) as OrderedPrice "
							+ "on CollectionCard.card_id = OrderedPrice.card_id "
							+ "and CollectionCard.rarity = OrderedPrice.rarity "
							+ "and CollectionCard.price_source_id = OrderedPrice.source_id "
							+ "and CollectionCard.set_id = OrderedPrice.set_id "
							+ "left outer join Location "
							+ "on CollectionCard.location_id = Location.id "
							+ "where CollectionCard.collection_id = " + id
							+ " group by CollectionCard.card_id, "
							+ "CollectionCard.rarity, "
							+ "CollectionCard.price_source_id, "
							+ "CollectionCard.set_id ");

			// fetch all events from database
			CollectionCardView ccv;

			while (rs.next()) {
				ccv = new CollectionCardView();
				ccv.setCollectionId(id);
				ccv.setCardId(rs.getInt(1));
				ccv.setAmount(rs.getInt(2));
				ccv.setRarity(rs.getString(3));
				ccv.setName(rs.getString(4));
				ccv.setAttribute(rs.getString(5));
				ccv.setPrice(rs.getBigDecimal(6));
				ccv.setLocationId(rs.getInt(7));
				ccv.setLocationName(rs.getString(8));
				ccv.setPriceSourceId(rs.getInt(9));
				ccv.setSetId(rs.getString(10));
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
