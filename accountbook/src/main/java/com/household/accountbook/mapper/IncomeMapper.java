package com.household.accountbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.household.accountbook.entity.Income;
import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.YearReport;
import com.household.accountbook.entity.IncomeMonthryAmountData;
import com.household.accountbook.entity.IncomeYearAmountData;

@Mapper
public interface IncomeMapper {

	@Insert("INSERT INTO income ("
			+ "account_id, income_category_name, "
			+ "income_amount, income_year, "
			+ "income_month, income_day"
			+ ") VALUES ("
			+ "#{accountId}, #{incomeCategoryName}, #{incomeAmount}, #{incomeYear}, #{incomeMonth}, #{incomeDay});")
	public void incomeRegister(Income income);
	
	//指定月のカテゴリごとの合計収入額を取得
		@Select("SELECT account_id, income_category_name, SUM(income_amount) AS income_amount "
				+ "FROM income "
				+ "GROUP BY account_id, income_category_name, income_year, income_month "
				+ "HAVING account_id = (SELECT id FROM accounts WHERE login_id = #{loginId}) AND income_year = #{year} AND income_month = #{month};")
		public List<IncomeMonthryAmountData> getMothryAmount(MonthryReport monthryReport);
		
		
		//指定年のカテゴリごとの合計収入額を取得
		@Select("SELECT account_id, income_category_name, SUM(income_amount) AS income_amount "
				+ "FROM income "
				+ "GROUP BY account_id, income_category_name, income_year "
				+ "HAVING account_id = (SELECT id FROM accounts WHERE login_id = #{loginId}) AND income_year = #{year};")
		public List<IncomeYearAmountData> getYearAmount(YearReport yearReport);	
		
		
}
