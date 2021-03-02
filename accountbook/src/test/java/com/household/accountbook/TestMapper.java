package com.household.accountbook;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.household.accountbook.entity.IncomeCategory;
import com.household.accountbook.entity.SpendingCategory;

@Mapper
public interface TestMapper {
	
	@Insert("INSERT INTO accounts (name, email, login_id, password) VALUES (#{name}, #{email}, #{loginId}, #{password})")
	public void testAccountRegister(String name, String email, String loginId, String password);
	
	@Select("INSERT INTO accounts (name, email, login_id, password) VALUES (#{name}, #{email}, #{loginId}, #{password}) returning id")
	public int testAccountRegisterGetId(String name, String email, String loginId, String password);
	
	@Delete("DELETE FROM accounts WHERE login_id = #{loginId}")
	public void testDataDelete(String loginId);
	
	@Insert("INSERT INTO spending_category VALUES(#{accountId}, #{spendingCategoryName}, #{spendingCategoryColor})")
	public void testDataSpendingRegister(SpendingCategory s);
	
	@Insert("INSERT INTO income_category VALUES(#{accountId}, #{incomeCategoryName}, #{incomeCategoryColor})")
	public void testDataIncomeRegister(IncomeCategory i);
	
	//デフォルト支出カテゴリ一覧取得
	@Select("SELECT default_spending_category_name as spendingCategoryName, default_spending_category_color as spendingCategoryColor FROM default_spending_category")
	public List<SpendingCategory> getDefaultSpendingCategory();

	//デフォルト支出カテゴリ一覧取得
	@Select("SELECT default_income_category_name as incomeCategoryName, default_income_category_color as incomeCategoryColor FROM default_income_category")
	public List<IncomeCategory> getDefaultIncomeCategory();
	
}
