package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.Deck;

public class DeckDAO extends DAO implements iDAO<Deck> {

	@Override
	public List<Deck> findAll() {
		List<Deck> allDecks = new ArrayList<Deck>();
		try {
			// get connection
		    Statement stmt = ds.getStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Deck");

			// fetch all events from database
			Deck deck;
			
			while (rs.next()) {
				deck = new Deck();
				deck.setId(rs.getInt(1));
				deck.setName(rs.getString(2));
				deck.setUserId(rs.getInt(3));
				allDecks.add(deck);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		return allDecks;
	}

	@Override
	public boolean delete(Deck entity) throws SQLException {
		return execute("DELETE FROM Deck WHERE id = '" + entity.getId() + "'");
	}

	@Override
	public Object insert(Deck entity) throws SQLException {
		return execute("INSERT INTO Deck(name,user_id) " +
                "VALUES ('" + entity.getName() + "'," + entity.getUserId() + ")");
	}

	@Override
	public boolean update(Deck entity) throws SQLException {
		return execute("UPDATE Deck SET name = '" + entity.getName() + 
                "', user_id = " + entity.getUserId() + 
                " where id = " + entity.getId());
	}

}
