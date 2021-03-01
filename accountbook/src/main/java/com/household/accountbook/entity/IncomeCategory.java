package com.household.accountbook.entity;

public class IncomeCategory {

	private int accountId;
	private String incomeCategoryName;
	private String incomeCategoryColor;
	

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

	public String getIncomeCategoryColor() {
		return incomeCategoryColor;
	}

	public void setIncomeCategoryColor(String incomeCategoryColor) {
		this.incomeCategoryColor = incomeCategoryColor;
	}

	public IncomeCategory(int accountId, String incomeCategoryName, String incomeCategoryColor) {
		super();
		this.accountId = accountId;
		this.incomeCategoryName = incomeCategoryName;
		this.incomeCategoryColor = incomeCategoryColor;
	}	

}
