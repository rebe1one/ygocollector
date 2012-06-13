package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.Card;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class CardDAO extends DAO implements iDAO<Card> {
	
	@Override
	public List<Card> findAll() {
		List<Card> allCards = new ArrayList<Card>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Card");

			// fetch all events from database
			Card card;
			
			while (rs.next()) {
				card = new Card();
				card.setId(rs.getInt(1));
		        card.setName(rs.getString(2));
				card.setSetId(rs.getString(3));
				card.setCardNumber(rs.getInt(4));
				card.setStars(rs.getInt(5));
				card.setDescription(rs.getString(6));
				card.setAttribute(rs.getString(7));
				allCards.add(card);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allCards;
	}
	
	public Card findSingle(List<Filter> filters) {
		List<Card> cards = find(filters);
		if (cards.size() > 0) {
			return cards.get(0);
		}
		return null;
	}
	
	public List<Card> find(List<Filter> filters) {
		List<Card> allCards = new ArrayList<Card>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM Card WHERE ", filters));

			// fetch all events from database
			Card card;
			
			while (rs.next()) {
				card = new Card();
				card.setId(rs.getInt(1));
		        card.setName(rs.getString(2));
				card.setCardNumber(rs.getInt(3));
				card.setStars(rs.getInt(4));
				card.setDescription(rs.getString(5));
				card.setAttribute(rs.getString(6));
				allCards.add(card);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allCards;
	}
	
	@Override
	public boolean delete(Card card) throws SQLException {
		return execute("DELETE FROM Card WHERE id = '" + card.getId() + "'");
	}
	
	@Override
	public Boolean insert(Card card) throws SQLException {
		return execute("INSERT INTO Card(name,set_id,card_number,stars,description,attribute) " +
                    "VALUES ('" + card.getName() + "','" + card.getSetId() +
                    "'," + card.getCardNumber() + "," + card.getStars() + 
                    ",'" + card.getDescription() + "','" + card.getAttribute() + "')");
	}
	
	@Override
	public boolean update(Card card) throws SQLException {
        return execute("UPDATE Card SET name = '" + card.getName() + 
                    "', set_id = " + card.getSetId() + ", card_number = " + card.getCardNumber() + 
                    ", stars = " + card.getStars() + ", description = '" + card.getDescription() + 
                    "', attribute = '" + card.getAttribute() + 
                    "' where id = '" + card.getId() + "'");
    }

}