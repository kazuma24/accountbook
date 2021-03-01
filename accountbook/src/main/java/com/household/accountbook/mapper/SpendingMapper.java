package com.household.accountbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.Spending;
import com.household.accountbook.entity.SpendingMonthryAmountData;
import com.household.accountbook.entity.SpendingYearAmountData;
import com.household.accountbook.entity.YearReport;

@Mapper
public interface SpendingMapper {

	@Insert("INSERT INTO spending (" + "account_id, spending_category_name, " + "spending_amount, spending_year, "
			+ "spending_month, spending_day" + ") VALUES ("
			+ "#{accountId}, #{spendingCategoryName}, #{spendingAmount}, #{spendingYear}, #{spendingMonth}, #{spendingDay});")
	public void spendingRegister(Spending spending);

	// 指定月のカテゴリごとの合計支出額を取得
	@Select("SELECT account_id, spending_category_name, SUM(spending_amount) AS spending_amount " + "FROM spending "
			+ "GROUP BY account_id, spending_category_name, spending_year, spending_month "
			+ "HAVING account_id = (SELECT id FROM accounts WHERE login_id = #{loginId}) AND spending_year = #{year} AND spending_month = #{month};")
	public List<SpendingMonthryAmountData> getMothryAmount(MonthryReport monthryReport);

	// 指年のカテゴリごとの合計支出額を取得
	@Select("SELECT account_id, spending_category_name, SUM(spending_amount) AS spending_amount " + "FROM spending "
			+ "GROUP BY account_id, spending_category_name, spending_year "
			+ "HAVING account_id = (SELECT id FROM accounts WHERE login_id = #{loginId}) AND spending_year = #{year};")
	public List<SpendingYearAmountData> getYearAmount(YearReport yearReport);
}
