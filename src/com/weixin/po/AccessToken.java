package com.weixin.po;

public class AccessToken {
	private String Token;
	private int expiresIn;
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	
}
