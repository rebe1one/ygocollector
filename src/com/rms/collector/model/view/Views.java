package com.rms.collector.model.view;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Views {
	private static HashMap<String, View> views = new HashMap<String, View>();;
	private static void build() {
		builCollectionCardView();
		buildDeckCardView();
	}
	
	private static void builCollectionCardView() {
		View collectionCardView = new View("CollectionCardView");
		Table cc = new Table("CollectionCard", "cc");
		cc.addColumn(new Column("collection_id"));
		cc.addColumn(new Column("card_id"));
		cc.addColumn(new Column("amount"));
		cc.addColumn(new Column("rarity"));
		cc.addColumn(new Column("price_source_id"));
		cc.addColumn(new Column("set_id"));
		collectionCardView.addTable(cc);
		Table c = new Table("Card", "c");
		c.addJoin(new Join("id", "card_id", "cc"));
		c.addColumn(new Column("name"));
		c.addColumn(new Column("attribute"));
		collectionCardView.addTable(c);
		Table l = new Table("Location", "l");
		l.addJoin(new Join("id", "location_id", "cc"));
		l.addColumn(new Column("id", "location_id"));
		l.addColumn(new Column("name", "location_name"));
		collectionCardView.addTable(l);
		Table ci = new Table("CardImage", "ci");
		ci.addJoin(new Join("card_id", "id", "c"));
		ci.addColumn(new Column("image_file_name"));
		collectionCardView.addTable(ci);
		Table op = new Table("Price", "op");
		op.setOrder(new Order("date", "desc"));
		op.addJoin(new Join("card_id", "cc"));
		op.addJoin(new Join("source_id", "price_source_id", "cc"));
		op.addJoin(new Join("rarity", "cc"));
		op.addJoin(new Join("set_id", "cc"));
		op.addColumn(new Column("price"));
		collectionCardView.addTable(op);
		views.put(collectionCardView.getName(), collectionCardView);
	}
	
	private static void buildDeckCardView() {
		View deckCardView = new View("DeckCardView");
		Table dc = new Table("DeckCard", "dc");
		dc.addColumn(new Column("deck_id"));
		dc.addColumn(new Column("card_id"));
		dc.addColumn(new Column("amount"));
		deckCardView.addTable(dc);
		Table c = new Table("Card", "c");
		c.addJoin(new Join("id", "card_id", "dc"));
		c.addColumn(new Column("name"));
		c.addColumn(new Column("attribute"));
		deckCardView.addTable(c);
		views.put(deckCardView.getName(), deckCardView);
	}
	
	public static void generate() throws SQLException {
		build();
		for(View view : views.values()) {
			view.create();
		}
	}
}
