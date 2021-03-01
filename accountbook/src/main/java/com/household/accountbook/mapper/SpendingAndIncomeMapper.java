package com.household.accountbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.household.accountbook.entity.IncomeForTheDay;
import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.MonthryTotal;
import com.household.accountbook.entity.SpendingForTheDay;
import com.household.accountbook.entity.YearReport;
import com.household.accountbook.entity.YearTotal;

@Mapper
public interface SpendingAndIncomeMapper {

	// 指定の月の合計収支を取得する
	@Select("WITH "
			+ "s AS (SELECT SUM(spending_amount) FROM spending WHERE account_id = #{accountId} AND spending_month = #{month} AND spending_year = #{year}), "
			+ "i AS (SELECT SUM(income_amount) FROM income WHERE account_id = #{accountId} AND income_month = #{month} AND income_year = #{year}) "
			+ "SELECT " + "COALESCE((SELECT * FROM s),0) AS totalMonthlySpending, "
			+ "COALESCE((SELECT * FROM i),0) AS totalMonthryIncome, "
			+ "(COALESCE((SELECT * FROM i),0) - COALESCE((SELECT * FROM s),0)) AS monthlyBalance, "
			+ "#{accountId} AS accountId;")
	public MonthryTotal getMonthryReport(MonthryReport monthryReport);

	// 指定の年の合計収支を取得する
	@Select("WITH "
			+ "s AS (SELECT SUM(spending_amount) FROM spending WHERE account_id = #{accountId} AND spending_year = #{year}), "
			+ "i AS (SELECT SUM(income_amount) FROM income WHERE account_id = #{accountId} AND income_year = #{year}) "
			+ "SELECT " + "COALESCE((SELECT * FROM s),0) AS totalYearSpending, "
			+ "COALESCE((SELECT * FROM i),0) AS totalYearIncome, "
			+ "(COALESCE((SELECT * FROM i),0) - COALESCE((SELECT * FROM s),0)) AS yearBalance, "
			+ "#{accountId} AS accountId;")
	public YearTotal getYearReport(YearReport yearReport);

	// 指定年月の各日にちの支出データを取得
	@Select("SELECT " + "SUM(spending_amount) as total, " + "account_id, "
			+ "concat(spending_year, to_char(spending_month,'00'), to_char(spending_day,'00')) as startdate "
			+ "FROM spending " + "WHERE account_id = #{accountId} " + "AND spending_year = #{year} "
			+ "AND spending_month = #{month} " + "GROUP BY "
			+ "spending_day, account_id, spending_year, spending_month, spending_day;")
	public List<SpendingForTheDay> getSpendingForTheDay(MonthryReport monthryReport);

	// 指定年月の各日にちの支出データを取得
	@Select("SELECT " + "SUM(income_amount) as total, " + "account_id, "
			+ "concat(income_year, to_char(income_month,'00'), to_char(income_day,'00')) as startdate " + "FROM income "
			+ "WHERE account_id = #{accountId} " + "AND income_year = #{year} " + "AND income_month = #{month} "
			+ "GROUP BY " + "income_day, account_id, income_year, income_month, income_day;")
	public List<IncomeForTheDay> getIncomeForTheDay(MonthryReport monthryReport);
}
