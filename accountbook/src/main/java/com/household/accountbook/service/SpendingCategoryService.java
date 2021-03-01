package com.household.accountbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.household.accountbook.entity.ChengeCategory;
import com.household.accountbook.entity.DeleteCategory;
import com.household.accountbook.entity.SpendingCategory;
import com.household.accountbook.mapper.SpendingCategoryMapper;

@Service
public class SpendingCategoryService {

	@Autowired
	SpendingCategoryMapper spendingCategoryMapper;

	@Transactional
	public List<SpendingCategory> DefaultCategorySettingAndGet(int id) {
		return spendingCategoryMapper.DefaultCategorySettingAndGet(id);
	}

	@Transactional
	public List<SpendingCategory> SetSpendingCategory(String loginId) {
		return spendingCategoryMapper.SetSpendingCategory(loginId);
	}

	@Transactional
	public List<SpendingCategory> GetRegisteredSpendingCategory(int accountId) {
		return spendingCategoryMapper.GetRegisteredspendingCategory(accountId);
	}

	@Transactional
	public List<SpendingCategory> addNewSpendingCategory(SpendingCategory spendingCategory) {
		return spendingCategoryMapper.addNewSpendingCategory(spendingCategory);
	}

	@Transactional
	public List<SpendingCategory> changeSpendingCategory(ChengeCategory chengeCategory) {
		return spendingCategoryMapper.changeSpendingCategory(chengeCategory);
	}

	@Transactional
	public List<SpendingCategory> deletedSpendingCategory(DeleteCategory deleteCategory) {
		return spendingCategoryMapper.deletedSpendingCategory(deleteCategory);
	}

	@Transactional
	public int checkTheRemainingNumber(int accountId) {
		return spendingCategoryMapper.checkTheRemainingNumber(accountId);
	}
}
