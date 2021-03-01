package com.household.accountbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.household.accountbook.entity.IncomeForTheDay;
import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.MonthryTotal;
import com.household.accountbook.entity.SpendingForTheDay;
import com.household.accountbook.entity.YearReport;
import com.household.accountbook.entity.YearTotal;
import com.household.accountbook.mapper.SpendingAndIncomeMapper;

@Service
public class SpendingAndIncomeService {

	@Autowired
	SpendingAndIncomeMapper spendingAndIncomeMapper;

	@Transactional
	public MonthryTotal getMonthryReport(MonthryReport monthryReport) {
		return spendingAndIncomeMapper.getMonthryReport(monthryReport);
	}

	@Transactional
	public YearTotal getYearReport(YearReport yearReport) {
		return spendingAndIncomeMapper.getYearReport(yearReport);
	}

	@Transactional
	public List<SpendingForTheDay> getSpendingForTheDay(MonthryReport monthryReport) {
		return spendingAndIncomeMapper.getSpendingForTheDay(monthryReport);
	}

	@Transactional
	public List<IncomeForTheDay> getIncomeForTheDay(MonthryReport monthryReport) {
		return spendingAndIncomeMapper.getIncomeForTheDay(monthryReport);
	}
}
