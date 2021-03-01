package com.household.accountbook.entity;

public class SpendingYearAmountData {

	private int accountId;
	private String spendingCategoryName;
	private String spendingAmount;

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

	public String getSpendingAmount() {
		return spendingAmount;
	}

	public void setSpendingAmount(String spendingAmount) {
		this.spendingAmount = spendingAmount;
	}
}
