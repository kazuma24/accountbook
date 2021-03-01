package com.household.accountbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.household.accountbook.entity.ChengeCategory;
import com.household.accountbook.entity.DeleteCategory;
import com.household.accountbook.entity.IncomeCategory;

@Mapper
public interface IncomeCategoryMapper {

	// デフォルト収入カテゴリテーブルの値を収入カテゴリテーブルにインサートして、インサートした値を取得（初期ユーザー）
	@Select("INSERT INTO income_category " + "(account_id, income_category_name, income_category_color) "
			+ "SELECT #{id}, default_income_category_name, default_income_category_color " + "FROM "
			+ "default_income_category " + "returning income_category_name, income_category_color")
	List<IncomeCategory> DefaultCategorySettingAndGet(int id);

	// 自分のIDで収入カテゴリテーブルに登録されているデータを取得（初期ユーザー以外）
	@Select("SELECT * FROM income_category WHERE account_id = (SELECT id FROM accounts WHERE login_id = #{loginId})")
	List<IncomeCategory> SetIncomeCategory(String loginId);

	@Select("SELECT * FROM income_category WHERE account_id = #{accountId}")
	List<IncomeCategory> GetRegisteredincomeCategory(int acountId);

	// 新規追加
	@Select("INSERT INTO income_category " + "VALUES ("
			+ "#{accountId}, #{incomeCategoryName}, #{incomeCategoryColor});"
			+ "SELECT * FROM income_category WHERE account_id = #{accountId}")
	List<IncomeCategory> addNewIncomeCategory(IncomeCategory incomeCategory);

	// カテゴリ名変更(incomeテーブルの登録済カテゴリも変更)
	@Select("UPDATE income_category " + "SET "
			+ "income_category_name = #{afterName}, income_category_color = #{afterColor} " + "WHERE "
			+ "account_id = #{accountId} AND income_category_name = #{beforeName}; " + "UPDATE income " + "SET "
			+ "income_category_name = #{afterName} " + "WHERE "
			+ "account_id = #{accountId} AND income_category_name = #{beforeName};" + "SELECT * FROM income_category "
			+ "WHERE " + "account_id = #{accountId}")
	List<IncomeCategory> changeIncomeCategory(ChengeCategory chengeCategory);

	// income,income_categoryテーブルから削除
	@Select("DELETE FROM income_category " + "WHERE "
			+ "account_id = #{accountId} AND income_category_name = #{deleteCategory}; "
			+ "DELETE FROM income WHERE account_id = #{accountId} AND income_category_name = #{deleteCategory};"
			+ "SELECT * FROM income_category WHERE account_id = #{accountId};")
	List<IncomeCategory> deletedIncomeCategory(DeleteCategory deleteCategory);

	// 残りカテゴリ数を取得
	@Select("SELECT COUNT(*) FROM income_category WHERE account_id = #{accountId};")
	int checkTheRemainingNumber(int accountId);
}
