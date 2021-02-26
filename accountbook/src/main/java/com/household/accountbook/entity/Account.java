package com.household.accountbook.entity;


public class Account {
	
	private int id;
	
	private String name;
	
	private String email;
	
	private String loginId;
	
	private String password;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Account(int id, String name, String email, String loginId, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.loginId = loginId;
		this.password = password;
	}
	
}
