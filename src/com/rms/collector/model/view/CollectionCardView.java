package com.rms.collector.model.view;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.rms.collector.model.CollectionCard;

public class CollectionCardView extends CollectionCard {
	private String name;
	private String attribute;
	private BigDecimal price;
	private String locationName;
	private String imageFileName;
	private Timestamp priceDate;
	private String userId;
	public CollectionCardView() { }
	public static CollectionCardView createViewFromCard(CollectionCard card) {
		CollectionCardView view = new CollectionCardView();
		view.setCollectionId(card.getCollectionId());
		view.setAmount(card.getAmount());
		view.setCardId(card.getCardId());
		view.setLocationId(card.getLocationId());
		view.setRarity(card.getRarity());
		return view;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Timestamp getPriceDate() {
		return priceDate;
	}
	public void setPriceDate(Timestamp priceDate) {
		this.priceDate = priceDate;
	}
}
