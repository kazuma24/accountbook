package com.household.accountbook.auth;

import java.util.Collection;

import org.springframework.security.core.userdetails.User;

import com.household.accountbook.entity.Account;

import org.springframework.security.core.GrantedAuthority;

public class DbAccountDetails extends User {

	private static final long serialVersionUID = 1L;
	// アカウント情報
	private final Account account;

	public DbAccountDetails(Account account, Collection<GrantedAuthority> authotities) {
		super(account.getLoginId(), account.getPassword(), true, true, true, true, authotities);
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}
}
