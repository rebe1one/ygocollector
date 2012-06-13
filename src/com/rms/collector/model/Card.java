package com.rms.collector.model;

public class Card {
	private int id;
	private String name;
	private int cardNumber;
	private int stars;
	private String description;
	private String attribute;
	private String setId;
	
	public Card() {
		
	}
	
	public Card(int id, String name, int cardNumber, int stars, String description, String attribute, String setId) {
		this.id = id;
		this.name = name;
		this.cardNumber = cardNumber;
		this.stars = stars;
		this.description = description;
		this.attribute = attribute;
		this.setId = setId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSetId() {
		return setId;
	}
	public void setSetId(String setId) {
		this.setId = setId;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("Card: ");
		strBuilder.append(this.name);
		strBuilder.append("; Card Number: ");
		strBuilder.append(this.cardNumber);
		strBuilder.append("; Attribute: ");
		strBuilder.append(this.attribute);
		strBuilder.append("; Level: ");
		strBuilder.append(this.stars);
		return strBuilder.toString();
	}
}
