package com.household.accountbook.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.error.ErrorMessages;
import com.household.accountbook.service.AccountService;

@Controller
public class IdCheckFromToMainController {

	@Autowired
	AccountService accountService;

	@GetMapping({ "/spendingedit/{accountId}", "/incomeedit/{accountId}" })
	public ModelAndView idCheckToMove(@PathVariable("accountId") Integer accountId, ModelAndView mav) {

		// 認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();

		// ログインIdとアカウントIdが一致するデータがあるか確認
		try {
			// SpringTest OK
			Optional<Object> opAccountId = Optional.ofNullable(accountService.idCollationCheck(accountId, loginId));
			System.out.println("idCheckToMove: opAccountId " + opAccountId);
			if (opAccountId.isPresent()) {
				int getedAccountId = (int) opAccountId.get();
				System.out.println("getedAccountId: " + getedAccountId);
				mav.setViewName("main");
				return mav;
			} else {
				// SpringTest OK
				mav.setViewName("apierror");
				mav.addObject("errorCode", 403);
				mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_403);
				return mav;
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("apierror");
			mav.addObject("errorCode", 500);
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_500);
			return mav;
		}

	}

	// 日時
	@GetMapping({ "/monthryspendingratio/{year}/{month}", "/monthryincomeratio/{year}/{month}" })
	public ModelAndView ratioFromMain(@PathVariable("year") int year, @PathVariable("month") int month,
			ModelAndView mav) {
		LocalDate localDate = LocalDate.now();
		int currentYear = localDate.getYear();
		List<Integer> years = Arrays.asList(currentYear - 2, currentYear - 1, currentYear, currentYear + 1,
				currentYear + 2);
		List<Integer> months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		if (!years.contains(year)) {
			// SpringTest OK
			mav.setViewName("apierror");
			mav.addObject("errorCode", 404);
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_404);
			return mav;
		}
		if (!months.contains(month)) {
			// SpringTest OK
			mav.setViewName("apierror");
			mav.addObject("errorCode", 404);
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_404);
			return mav;
		}
		// SpringTest OK
		mav.setViewName("main");
		return mav;
	}
}
