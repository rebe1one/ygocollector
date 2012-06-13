package com.rms.collector.model;

public class CollectionCard {
	private int collectionId;
	private int cardId;
	private int amount;
	private String rarity;
	public CollectionCard() {
		super();
	}
	public CollectionCard(int collectionId, int cardId, int amount,
			String rarity) {
		super();
		this.collectionId = collectionId;
		this.cardId = cardId;
		this.amount = amount;
		this.rarity = rarity;
	}
	public int getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
}
