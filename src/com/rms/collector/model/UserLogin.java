package com.rms.collector.model;

public class UserLogin {
	int userId;
	String userLogin;
	String password;
	
	public UserLogin() {
		
	}
	
	public UserLogin(int userId, String userLogin, String password) {
		this.userId = userId;
		this.userLogin = userLogin;
		this.password = password;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
