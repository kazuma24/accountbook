package com.household.accountbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.household.accountbook.entity.Income;
import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.YearReport;
import com.household.accountbook.entity.IncomeMonthryAmountData;
import com.household.accountbook.entity.IncomeYearAmountData;
import com.household.accountbook.mapper.IncomeMapper;

@Service
public class IncomeService {

	@Autowired
	IncomeMapper incomeMapper;

	@Transactional
	public void incomeRegister(Income income) {
		incomeMapper.incomeRegister(income);
	}

	@Transactional
	public List<IncomeMonthryAmountData> getMothryAmount(MonthryReport monthryReport) {
		return incomeMapper.getMothryAmount(monthryReport);
	}

	@Transactional
	public List<IncomeYearAmountData> getYearAmount(YearReport yearReport) {
		return incomeMapper.getYearAmount(yearReport);
	}
}
