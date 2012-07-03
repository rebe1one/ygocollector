package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.CardImage;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class CardImageDAO extends DAO implements iDAO<CardImage> {
	
	@Override
	public List<CardImage> findAll() {
		List<CardImage> allImages = new ArrayList<CardImage>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CardImage");

			// fetch all events from database
			CardImage images;
			
			while (rs.next()) {
				images = new CardImage();
				images.setCardId(rs.getInt(1));
		        images.setImageFileName(rs.getString(2));
				allImages.add(images);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allImages;
	}
	
	public CardImage findSingle(List<Filter> filters) {
		List<CardImage> cards = find(filters);
		if (cards.size() > 0) {
			return cards.get(0);
		}
		return null;
	}
	
	public List<CardImage> find(List<Filter> filters) {
		List<CardImage> allImages = new ArrayList<CardImage>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM CardImage WHERE ", filters));

			// fetch all events from database
			CardImage images;
			
			while (rs.next()) {
				images = new CardImage();
				images.setCardId(rs.getInt(1));
		        images.setImageFileName(rs.getString(2));
				allImages.add(images);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allImages;
	}
	
	@Override
	public boolean delete(CardImage image) throws SQLException {
		return execute("DELETE FROM CardImage WHERE card_id = '" + image.getCardId() + "'");
	}
	
	@Override
	public Boolean insert(CardImage image) throws SQLException {
		return execute("INSERT INTO CardImage(card_id,image_file_name) " +
                    "VALUES ('" + image.getCardId() + "','" + image.getImageFileName() + "')");
	}
	
	@Override
	public boolean update(CardImage image) throws SQLException {
        return execute("UPDATE CardImage SET image_file_name = '" + image.getImageFileName() + 
                    "' where card_id = '" + image.getCardId() + "'");
    }

}