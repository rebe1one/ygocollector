package com.rms.collector.view;

import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import com.rms.collector.data.CardDAO;
import com.rms.collector.model.Card;

public class CardViewModel {
	
	private CardDAO cardDao = new CardDAO();
	
	private Card selectedCard, newCard = new Card();
	
	public Card getSelectedCard() {
		return selectedCard;
	}

	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}
	
	public Card getNewCard() {
		return newCard;
	}

	public void setNewCard(Card newCard) {
		this.newCard = newCard;
	}

	public List<Card> getCards() {
		return cardDao.findAll();
	}
	
	@Command("add")
	@NotifyChange("cards")
	public void add() {
		cardDao.insert(this.newCard);
		this.newCard = new Card();
	}
	
	@Command("update")
	@NotifyChange("cards")
	public void update() {
		cardDao.update(this.selectedCard);
	}
	
	@Command("delete")
	@NotifyChange({"cards", "selectedCard"})
	public void delete() {
		//shouldn't be able to delete with selectedCard being null anyway
		//unless trying to hack the system, so just ignore the request
		if(this.selectedCard != null) {
			cardDao.delete(this.selectedCard);
			this.selectedCard = null;
		}
	}
}