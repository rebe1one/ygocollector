package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.DeckCard;
import com.rms.collector.model.DeckCard;
import com.rms.collector.model.DeckCard;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class DeckCardDAO extends DAO implements iDAO<DeckCard> {

	@Override
	public List<DeckCard> findAll() {
		List<DeckCard> allDecks = new ArrayList<DeckCard>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM DeckCard");

			// fetch all events from database
			DeckCard deckCard;
			
			while (rs.next()) {
				deckCard = new DeckCard();
				deckCard.setDeckId(rs.getInt(1));
				deckCard.setCardId(rs.getInt(2));
				deckCard.setAmount(rs.getInt(3));
				allDecks.add(deckCard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allDecks;
	}

	public DeckCard findSingle(List<Filter> filters) {
		List<DeckCard> cards = find(filters);
		if (cards.size() > 0) {
			return cards.get(0);
		}
		return null;
	}
	
	public List<DeckCard> find(List<Filter> filters) {
		List<DeckCard> allCards = new ArrayList<DeckCard>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM DeckCard WHERE ", filters));

			DeckCard deckCard;
			
			while (rs.next()) {
				deckCard = new DeckCard();
				deckCard.setDeckId(rs.getInt(1));
				deckCard.setCardId(rs.getInt(2));
				deckCard.setAmount(rs.getInt(3));
				allCards.add(deckCard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allCards;
	}
	
	@Override
	public boolean delete(DeckCard entity) throws SQLException {
		return execute("DELETE FROM DeckCard WHERE deck_id = '" + entity.getDeckId() + "' AND card_id = '" + entity.getCardId() + "'");
	}

	@Override
	public Object insert(DeckCard entity) throws SQLException {
		return executeReturn("INSERT INTO DeckCard(deck_id,card_id,amount) " +
                "VALUES ('" + entity.getDeckId() + "','" + entity.getCardId() + "','" + entity.getAmount() + "')");
	}

	@Override
	public boolean update(DeckCard entity) throws SQLException {
		return execute("UPDATE DeckCard SET amount = '" + entity.getAmount() + 
                "' where deck_id = '" + entity.getDeckId() + 
                "' AND card_id = '" + entity.getCardId() + "'");
	}

}
