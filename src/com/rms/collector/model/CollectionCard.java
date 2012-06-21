package com.rms.collector.model;

public class CollectionCard {
	protected int collectionId;
	protected int cardId;
	protected int amount;
	protected String rarity;
	protected int locationId;
	protected int priceSourceId;
	protected String setId = "";
	public CollectionCard() {
		super();
	}
	public CollectionCard(int collectionId, int cardId, int amount,
			String rarity, int locationId, int priceSourceId, String setId) {
		super();
		this.collectionId = collectionId;
		this.cardId = cardId;
		this.amount = amount;
		this.rarity = rarity;
		this.locationId = locationId;
		this.priceSourceId = priceSourceId;
		this.setId = setId;
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
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public int getPriceSourceId() {
		return priceSourceId;
	}
	public void setPriceSourceId(int priceSourceId) {
		this.priceSourceId = priceSourceId;
	}
	public String getSetId() {
		return setId;
	}
	public void setSetId(String setId) {
		this.setId = setId;
	}
	public String toString() {
		return "CollectionCard = " + 
				"collection_id:'" + this.collectionId + "'" +
				"card_id:'" + this.cardId + "'" +
				"amount:'" + this.amount + "'" +
				"rarity:'" + this.rarity + "'" +
				"locationId:'" + this.locationId + "'" +
				"priceSourceId:'" + this.priceSourceId + "'" +
				"set_id:'" + this.setId + "'";
	}
}
