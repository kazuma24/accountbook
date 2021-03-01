package com.household.accountbook;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
