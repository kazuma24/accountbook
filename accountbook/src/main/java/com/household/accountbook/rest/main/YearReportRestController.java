package com.household.accountbook.rest.main;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(value="/yeargettotal", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Object getYearReport(@RequestBody YearReport yearReport) {
		System.out.println("YearReportRestController: getYearReport");
		try {
			Optional<Integer> id = Optional.ofNullable(yearReport.getAccountId());
			Optional<String> loginId = Optional.ofNullable(yearReport.getLoginId());
			Optional<Integer> year = Optional.ofNullable(yearReport.getYear());
			if(!id.isPresent() || !loginId.isPresent() || !year.isPresent()) {
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}
			// 空チェック
			if (Arrays.asList(id.get(), loginId.get(), year.get()).stream().filter(s -> s.equals("") || s.equals(0)).count() > 0) {
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}
			// ID照合
			Object obj = authenticationInformation.iDVerificationcheck(AuthenticationInformation.getAuthenticationInformationLoginId(), id.get());
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
