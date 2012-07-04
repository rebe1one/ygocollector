package com.rms.collector.model;

import java.sql.Blob;
import java.sql.Timestamp;

public class UserEBayInfo {
	int userId;
	String eBayToken;
	Timestamp eBayTokenExpirationDate;
	
	public UserEBayInfo() {
		
	}
	
	public UserEBayInfo(int userId, String eBayToken, Timestamp eBayTokenExpirationDate) {
		this.userId = userId;
		this.eBayToken = eBayToken;
		this.eBayTokenExpirationDate = eBayTokenExpirationDate;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getEBayToken() {
		return eBayToken;
	}
	public void setEBayToken(byte[] token) {
		this.eBayToken = new String(token);
	}
	public Timestamp getEBayTokenExpirationDate() {
		return eBayTokenExpirationDate;
	}
	public void setEBayTokenExpirationDate(Timestamp date) {
		this.eBayTokenExpirationDate = date;
	}
}
