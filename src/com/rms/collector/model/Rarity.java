package com.rms.collector.model;

public class Rarity {
	private String rarity;
	private String description;
	public Rarity() {
		super();
	}
	public Rarity(String rarity, String description) {
		super();
		this.rarity = rarity;
		this.description = description;
	}
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
