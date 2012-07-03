package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.view.DeckCardView;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class DeckCardViewDAO extends DAO {

	public List<DeckCardView> findAll() {
		List<DeckCardView> allDecks = new ArrayList<DeckCardView>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM DeckCardView");

			// fetch all events from database
			DeckCardView deckCard;
			
			while (rs.next()) {
				deckCard = new DeckCardView();
				deckCard.setDeckId(rs.getInt(1));
				deckCard.setCardId(rs.getInt(2));
				deckCard.setAmount(rs.getInt(3));
				deckCard.setName(rs.getString(4));
				deckCard.setAttribute(rs.getString(5));
				allDecks.add(deckCard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allDecks;
	}

	public DeckCardView findSingle(List<Filter> filters) {
		List<DeckCardView> cards = find(filters);
		if (cards.size() > 0) {
			return cards.get(0);
		}
		return null;
	}
	
	public List<DeckCardView> find(List<Filter> filters) {
		List<DeckCardView> allCards = new ArrayList<DeckCardView>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery(Util.buildSQLString("SELECT * FROM DeckCardView WHERE ", filters));

			DeckCardView deckCard;
			
			while (rs.next()) {
				deckCard = new DeckCardView();
				deckCard.setDeckId(rs.getInt(1));
				deckCard.setCardId(rs.getInt(2));
				deckCard.setAmount(rs.getInt(3));
				deckCard.setName(rs.getString(4));
				deckCard.setAttribute(rs.getString(5));
				allCards.add(deckCard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allCards;
	}
}
