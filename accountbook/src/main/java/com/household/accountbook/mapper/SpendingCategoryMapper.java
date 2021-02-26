package com.household.accountbook.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.household.accountbook.entity.ChengeCategory;
import com.household.accountbook.entity.DeleteCategory;
import com.household.accountbook.entity.SpendingCategory;

@Mapper
public interface SpendingCategoryMapper {
	
	//デフォルト支出カテゴリテーブルの値を支出カテゴリテーブルにインサートして、インサートした値を取得（初期ユーザー）
	@Select("INSERT INTO spending_category "
			+ "(account_id, spending_category_name, spending_category_color) "
			+ "SELECT #{id}, default_spending_category_name, default_spending_category_color "
			+ "FROM "
			+ "default_spending_category "
			+ "returning spending_category_name, spending_category_color") 
	List<SpendingCategory> DefaultCategorySettingAndGet(int id);
	
	//自分のIDで支出カテゴリテーブルに登録されているデータを取得（初期ユーザー以外）
	@Select("SELECT * FROM spending_category WHERE account_id = (SELECT id FROM accounts WHERE login_id = #{loginId})")
	List<SpendingCategory> SetSpendingCategory(String loginId);
	
	@Select("SELECT * FROM spending_category WHERE account_id = #{accountId}")
	List<SpendingCategory> GetRegisteredspendingCategory(int acountId);

	//新規追加
	@Select("INSERT INTO spending_category "
			+ "VALUES ("
			+ "#{accountId}, #{spendingCategoryName}, #{spendingCategoryColor});"
			+ "SELECT * FROM spending_category WHERE account_id = #{accountId}"
			)
	List<SpendingCategory> addNewSpendingCategory(SpendingCategory spendingCategory);
	
	//カテゴリ名変更(spendingテーブルの登録済カテゴリも変更)
	@Select("UPDATE spending_category "
			+ "SET "
			+ "spending_category_name = #{afterName}, spending_category_color = #{afterColor} "
			+ "WHERE "
			+ "account_id = #{accountId} AND spending_category_name = #{beforeName}; "
			+ "UPDATE spending "
			+ "SET "
			+ "spending_category_name = #{afterName} "
			+ "WHERE "
			+ "account_id = #{accountId} AND spending_category_name = #{beforeName};"
			+ "SELECT * FROM spending_category "
			+ "WHERE "
			+ "account_id = #{accountId}"
			)
	List<SpendingCategory> changeSpendingCategory(ChengeCategory chengeCategory);
	
	//spending,spending_categoryテーブルから削除
	@Select("DELETE FROM spending_category "
			+ "WHERE "
			+ "account_id = #{accountId} AND spending_category_name = #{deleteCategory}; "
			+ "DELETE FROM spending WHERE account_id = #{accountId} AND spending_category_name = #{deleteCategory};"
			+ "SELECT * FROM spending_category WHERE account_id = #{accountId};"
			)
	List<SpendingCategory> deletedSpendingCategory(DeleteCategory deleteCategory);
	
	//残りカテゴリ数を取得
	@Select("SELECT COUNT(*) FROM spending_category WHERE account_id = #{accountId};")
	int  checkTheRemainingNumber(int accountId);
	
	
	
}
