package com.household.accountbook.entity;

public class SpendingCategory {

	private int accountId;
	private String spendingCategoryName;
	private String spendingCategoryColor;

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getSpendingCategoryName() {
		return spendingCategoryName;
	}

	public void setSpendingCategoryName(String spendingCategoryName) {
		this.spendingCategoryName = spendingCategoryName;
	}

	public String getSpendingCategoryColor() {
		return spendingCategoryColor;
	}

	public void setSpendingCategoryColor(String spendingCategoryColor) {
		this.spendingCategoryColor = spendingCategoryColor;
	}

}
