package com.household.accountbook.entity;

public class Income {

	private int id;
	private int accountId;
	private String incomeCategoryName;
	private int incomeAmount;
	private int incomeYear;
	private int incomeMonth;
	private int incomeDay;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getIncomeCategoryName() {
		return incomeCategoryName;
	}
	public void setIncomeCategoryName(String incomeCategoryName) {
		this.incomeCategoryName = incomeCategoryName;
	}
	public int getIncomeAmount() {
		return incomeAmount;
	}
	public void setIncomeAmount(int incomeAmount) {
		this.incomeAmount = incomeAmount;
	}
	public int getIncomeYear() {
		return incomeYear;
	}
	public void setIncomeYear(int incomeYear) {
		this.incomeYear = incomeYear;
	}
	public int getIncomeMonth() {
		return incomeMonth;
	}
	public void setIncomeMonth(int incomeMonth) {
		this.incomeMonth = incomeMonth;
	}
	public int getIncomeDay() {
		return incomeDay;
	}
	public void setIncomeDay(int incomeDay) {
		this.incomeDay = incomeDay;
	}
	
	
	
}
