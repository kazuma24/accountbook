package com.household.accountbook;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface TestMapper {
	
	@Insert("INSERT INTO accounts (name, email, login_id, password) VALUES (#{name}, #{email}, #{loginId}, #{password})")
	public void testAccountRegister(String name, String email, String loginId, String password);
	
	@Select("INSERT INTO accounts (name, email, login_id, password) VALUES (#{name}, #{email}, #{loginId}, #{password}) returning id")
	public int testAccountRegisterGetId(String name, String email, String loginId, String password);
}
