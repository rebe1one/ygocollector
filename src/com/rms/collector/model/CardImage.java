package com.rms.collector.model;

public class CardImage {
	private int cardId;
	private String imageFileName;
	public CardImage(int cardId, String imageFileName) {
		this.cardId = cardId;
		this.imageFileName = imageFileName;
	}
	public CardImage() {}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
}
