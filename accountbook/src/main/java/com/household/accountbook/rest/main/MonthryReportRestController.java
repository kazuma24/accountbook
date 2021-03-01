package com.household.accountbook.rest.main;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.MonthryTotal;
import com.household.accountbook.error.ErrorMessages;
import com.household.accountbook.service.SpendingAndIncomeService;

@RestController
public class MonthryReportRestController {

	@Autowired
	AuthenticationInformation authenticationInformation;

	@Autowired
	ApiError apiError;

	@Autowired
	SpendingAndIncomeService spendingAndIncomeService;

	@RequestMapping(value = "/gettotal", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Object getMonthryReport(@RequestBody MonthryReport monthryReport) {
		System.out.println("MonthryReportRestController: getMonthryReport");

		// ID照合
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		try {
			// 不備なデータがないか
			Long count = Stream.of(monthryReport.getAccountId(), monthryReport.getYear(), monthryReport.getMonth())
					.filter(s -> s == 0).count();
			if (count > 0) {
				// Spring Test OK
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}

			// ID照合
			Object obj = authenticationInformation.iDVerificationcheck(loginId, monthryReport.getAccountId());
			if (obj instanceof ApiError) {
				// Spring Test OK
				return apiError = (ApiError) obj;
			}

			// 指定月の支出/収入/収支/合計値を取得
			MonthryTotal monthrytotal = spendingAndIncomeService.getMonthryReport(monthryReport);
			return monthrytotal;

		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}
}
