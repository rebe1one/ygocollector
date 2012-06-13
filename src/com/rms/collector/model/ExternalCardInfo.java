package com.rms.collector.model;

import java.math.BigDecimal;

public class ExternalCardInfo {
	private String name;
	private BigDecimal price;
	private String setId;
	private String rarity;
	public ExternalCardInfo(String name, BigDecimal price, String setId,
			String rarity) {
		this.name = name;
		this.price = price;
		this.setId = setId;
		this.rarity = rarity;
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
	public String getSetId() {
		return setId;
	}
	public void setSetId(String setId) {
		this.setId = setId;
	}
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(price);
		b.append(" ");
		b.append(name);
		b.append(" ");
		b.append(setId);
		b.append(" ");
		b.append(rarity);
		return b.toString();
	}
}
