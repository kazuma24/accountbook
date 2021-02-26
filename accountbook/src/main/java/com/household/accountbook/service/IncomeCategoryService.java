package com.household.accountbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.household.accountbook.entity.ChengeCategory;
import com.household.accountbook.entity.DeleteCategory;
import com.household.accountbook.entity.IncomeCategory;
import com.household.accountbook.mapper.IncomeCategoryMapper;

@Service
public class IncomeCategoryService {

	@Autowired
	IncomeCategoryMapper incomeCategoryMapper;
	
	@Transactional
	public List<IncomeCategory> DefaultCategorySettingAndGet(int id) {
		return incomeCategoryMapper.DefaultCategorySettingAndGet(id);
	}
	
	@Transactional
	public List<IncomeCategory> SetIncomeCategory(String loginId) {
		return incomeCategoryMapper.SetIncomeCategory(loginId);
	}
	
	@Transactional
	public List<IncomeCategory> GetRegisteredIncomeCategory(int accountId) {
		return incomeCategoryMapper.GetRegisteredincomeCategory(accountId);
	}
	
	@Transactional
	public List<IncomeCategory> addNewIncomeCategory(IncomeCategory incomeCategory) {
		return incomeCategoryMapper.addNewIncomeCategory(incomeCategory);
	}
	
	@Transactional
	public List<IncomeCategory> changeIncomeCategory(ChengeCategory chengeCategory) {
		return incomeCategoryMapper.changeIncomeCategory(chengeCategory);
	}
	
	@Transactional
	public List<IncomeCategory> deletedIncomeCategory(DeleteCategory deleteCategory) {
		return incomeCategoryMapper.deletedIncomeCategory(deleteCategory);
	}

	@Transactional
	public int checkTheRemainingNumber(int accountId) {
		return incomeCategoryMapper.checkTheRemainingNumber(accountId);
	}
}
