package com.rms.collector.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Price {
	private int cardId;
	private int sourceId;
	private String edition;
	private BigDecimal price;
	private Timestamp date;
	private String rarity;
	private String setId;
	
	public Price(int cardId, int sourceId, String edition, BigDecimal price,
			Timestamp date, String rarity, String setId) {
		super();
		this.cardId = cardId;
		this.sourceId = sourceId;
		this.edition = edition;
		this.price = price;
		this.date = date;
		this.rarity = rarity;
		this.setId = setId;
	}
	public Price() {
		// TODO Auto-generated constructor stub
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	public String getSetId() {
		return setId;
	}
	public void setSetId(String setId) {
		this.setId = setId;
	}
}
