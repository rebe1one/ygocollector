package com.rms.collector.model.view;

import com.rms.collector.model.DeckCard;

public class DeckCardView extends DeckCard {
	private String name;
	private String attribute;
	public DeckCardView() { }
	public static DeckCardView createViewFromCard(DeckCard card) {
		DeckCardView view = new DeckCardView();
		view.setDeckId(card.getDeckId());
		view.setAmount(card.getAmount());
		view.setCardId(card.getCardId());
		return view;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}
