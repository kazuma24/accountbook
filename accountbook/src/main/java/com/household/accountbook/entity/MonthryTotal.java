package com.household.accountbook.entity;

public class MonthryTotal {

	private int accountId;
	private int totalMonthlySpending;
	private int totalMonthryIncome;
	private int monthlyBalance;

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getTotalMonthlySpending() {
		return totalMonthlySpending;
	}

	public void setTotalMonthlySpending(int totalMonthlySpending) {
		this.totalMonthlySpending = totalMonthlySpending;
	}

	public int getTotalMonthryIncome() {
		return totalMonthryIncome;
	}

	public void setTotalMonthryIncome(int totalMonthryIncome) {
		this.totalMonthryIncome = totalMonthryIncome;
	}

	public int getMonthlyBalance() {
		return monthlyBalance;
	}

	public void setMonthlyBalance(int monthlyBalance) {
		this.monthlyBalance = monthlyBalance;
	}

}
