package com.household.accountbook.rest.calendar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.Account;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.IncomeForTheDay;
import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.SpendingForTheDay;
import com.household.accountbook.error.ErrorMessages;
import com.household.accountbook.service.AccountService;
import com.household.accountbook.service.SpendingAndIncomeService;

@RestController
public class CalendarRestController {

	@Autowired
	AccountService accountService;

	@Autowired
	SpendingAndIncomeService spendingAndIncomeService;

	@Autowired
	ApiError apiError;

	@PostMapping("/spendingcalendardata")
	public Object getSpendingCalendarData(@RequestBody MonthryReport monthryReport) {
		// 認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		try {
			// loginIdからid取得
			Account account = accountService.loginExamination(loginId);
			// 指定されている日付の収支を取得
			monthryReport.setAccountId(account.getId());
			List<SpendingForTheDay> spendingForTheDayList = spendingAndIncomeService
					.getSpendingForTheDay(monthryReport);
			return spendingForTheDayList;
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			apiError.setMessage(ErrorMessages.ERRORMESSAGE_500);
			return apiError;
		}
	}

	@PostMapping("/incomecalendardata")
	public Object getIncomeCalendarData(@RequestBody MonthryReport monthryReport) {
		// 認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		try {
			// loginIdからid取得
			Account account = accountService.loginExamination(loginId);
			// 指定されている日付の収支を取得
			monthryReport.setAccountId(account.getId());
			List<IncomeForTheDay> incomeForTheDayList = spendingAndIncomeService.getIncomeForTheDay(monthryReport);
			return incomeForTheDayList;
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			apiError.setMessage(ErrorMessages.ERRORMESSAGE_500);
			return apiError;
		}
	}
}
