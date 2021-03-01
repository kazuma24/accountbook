package com.household.accountbook.rest.ratio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.SpendingMonthryAmountData;
import com.household.accountbook.entity.SpendingYearAmountData;
import com.household.accountbook.entity.YearReport;
import com.household.accountbook.service.AccountService;
import com.household.accountbook.service.SpendingCategoryService;
import com.household.accountbook.service.SpendingService;

@RestController
public class SpendingRatioRestController {

	@Autowired
	AccountService accountService;

	@Autowired
	ApiError apiError;

	@Autowired
	SpendingService spendingService;

	@Autowired
	SpendingCategoryService spendingCategoryService;

	// 支出割合画面(月間）
	@PostMapping("/acquisitionofeachamountdata")
	public Object getAcquisitionOfEachAmountData(@RequestBody MonthryReport monthryReport) {// 認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();

		try {
			monthryReport.setLoginId(loginId);
			List<SpendingMonthryAmountData> opSpendingMonthryAmountData = spendingService
					.getMothryAmount(monthryReport);
			System.out.println("Data " + opSpendingMonthryAmountData);
			return opSpendingMonthryAmountData;
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}

	// 支出割合画面(年間）
	@PostMapping("/yearacquisitionofeachamountdata")
	public Object getYearAcquisitionOfEachAmountData(@RequestBody YearReport yearReport) {// 認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();

		try {
			yearReport.setLoginId(loginId);
			List<SpendingYearAmountData> opSpendingYearAmountData = spendingService.getYearAmount(yearReport);
			System.out.println("Data " + opSpendingYearAmountData);
			return opSpendingYearAmountData;
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}

}
