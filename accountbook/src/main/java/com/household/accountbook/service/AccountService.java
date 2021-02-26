package com.household.accountbook.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.household.accountbook.entity.Account;
import com.household.accountbook.mapper.AccountMapper;

@Service
public class AccountService {

	@Autowired
	AccountMapper accountMapper;
	
	@Transactional
	public int accountRegister(Account account) {
		return accountMapper.accountRegister(account);
	}
	
	@Transactional
	public Account fetchAccount(int id) {
		return accountMapper.fetchAccount(id);
	}
	
	@Transactional
	public int loginIdDuplicateExamination(String loginId) {
		return accountMapper.loginIdDuplicateExamination(loginId);
	}
	
	@Transactional
	public Account loginExamination(String loginId) {
		return accountMapper.loginExamination(loginId);
	}
	
	@Transactional
	public Object spendingCategoryCheckAndIdAcquisition(String loginId) {
		return accountMapper.spendingCategoryCheckAndIdAcquisition(loginId);
	}
	
	@Transactional
	public Object incomeCategoryCheckAndIdAcquisition(String loginId) {
		return accountMapper.incomeCategoryCheckAndIdAcquisition(loginId);
	}
	
	@Transactional
	public Object idCollationCheck(Integer accountId, String loginId) {
		return accountMapper.idCollationCheck(accountId, loginId);
	}
	
	@Transactional
	public void allDelete(Integer accountId) {
		accountMapper.allDelete(accountId);
	}
}
