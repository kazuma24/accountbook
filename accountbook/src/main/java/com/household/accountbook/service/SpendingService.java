package com.household.accountbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.Spending;
import com.household.accountbook.entity.SpendingMonthryAmountData;
import com.household.accountbook.entity.SpendingYearAmountData;
import com.household.accountbook.entity.YearReport;
import com.household.accountbook.mapper.SpendingMapper;

@Service
public class SpendingService {

	@Autowired
	SpendingMapper spendingMapper;

	@Transactional
	public void spendingRegister(Spending spending) {
		spendingMapper.spendingRegister(spending);
	}

	@Transactional
	public List<SpendingMonthryAmountData> getMothryAmount(MonthryReport monthryReport) {
		return spendingMapper.getMothryAmount(monthryReport);
	}

	@Transactional
	public List<SpendingYearAmountData> getYearAmount(YearReport yearReport) {
		return spendingMapper.getYearAmount(yearReport);
	}
}
