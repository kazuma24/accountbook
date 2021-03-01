package com.household.accountbook.rest.ratio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.YearReport;
import com.household.accountbook.entity.IncomeMonthryAmountData;
import com.household.accountbook.entity.IncomeYearAmountData;
import com.household.accountbook.service.AccountService;
import com.household.accountbook.service.IncomeCategoryService;
import com.household.accountbook.service.IncomeService;

@RestController
public class IncomeRatioRestController {

	@Autowired
	AccountService accountService;

	@Autowired
	ApiError apiError;

	@Autowired
	IncomeService incomeService;

	@Autowired
	IncomeCategoryService incomeCategoryService;

	// 収入割合画面(月間)
	@PostMapping("/incomeacquisitionofeachamountdata")
	public Object getAcquisitionOfEachAmountData(@RequestBody MonthryReport monthryReport) {// 認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();

		try {
			monthryReport.setLoginId(loginId);
			List<IncomeMonthryAmountData> opIncomeMonthryAmountData = incomeService.getMothryAmount(monthryReport);
			System.out.println("Data " + opIncomeMonthryAmountData);
			return opIncomeMonthryAmountData;
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}

	// 収入割合画面(年間)
	@PostMapping("/yearincomeacquisitionofeachamountdata")
	public Object getYearAcquisitionOfEachAmountData(@RequestBody YearReport yearReport) {// 認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();

		try {
			yearReport.setLoginId(loginId);
			List<IncomeYearAmountData> opIncomeYearAmountData = incomeService.getYearAmount(yearReport);
			System.out.println("Data " + opIncomeYearAmountData);
			return opIncomeYearAmountData;
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}

}