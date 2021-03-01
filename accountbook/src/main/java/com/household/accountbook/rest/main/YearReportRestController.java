package com.household.accountbook.rest.main;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.YearReport;
import com.household.accountbook.entity.YearTotal;
import com.household.accountbook.error.ErrorMessages;
import com.household.accountbook.service.SpendingAndIncomeService;

@RestController
public class YearReportRestController {

	@Autowired
	AuthenticationInformation authenticationInformation;

	@Autowired
	ApiError apiError;

	@Autowired
	SpendingAndIncomeService spendingAndIncomeService;

	@RequestMapping("/yeargettotal")
	public Object getYearReport(@RequestBody YearReport yearReport) {
		System.out.println("YearReportRestController: getYearReport");

		// ID照合
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		try {
			// 不備なデータがないか
			Long count = Stream.of(yearReport.getAccountId(), yearReport.getYear()).filter(s -> s == null || s == 0)
					.count();
			if (count > 0) {
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}

			// ID照合
			Object obj = authenticationInformation.iDVerificationcheck(loginId, yearReport.getAccountId());
			if (obj instanceof ApiError) {
				return apiError = (ApiError) obj;
			}

			// 指定月の支出/収入/収支/合計値を取得
			YearTotal yeartotal = spendingAndIncomeService.getYearReport(yearReport);
			return yeartotal;

		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}
}
