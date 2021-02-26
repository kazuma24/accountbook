package com.household.accountbook.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.household.accountbook.entity.Account;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.service.AccountService;
import com.household.accountbook.service.SpendingCategoryService;

public class AuthenticationInformation {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	SpendingCategoryService spendingCategoryService;
	
	@Autowired
	ApiError apiError;

	//認証情報からログインIDを取得
	public static String getAuthenticationInformationLoginId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String loginId = "";
		if (principal instanceof UserDetails) {
		  loginId = ((UserDetails)principal).getUsername();
		} else {
		  loginId = principal.toString(); //anonymousUser
		}
		return loginId;
	}
	
	//認証情報ログインIDをもとにリクエストのIDを照合
	/**
	 * 
	 * @param loginId
	 * @param accountId
	 * @return NG apiError
	 * @return OK accountId
	 */
	public Object iDVerificationcheck(String loginId, int accountId) {
		try {
			Account account = accountService.loginExamination(loginId);
			//URLのIdと認証済のID照合
			System.out.println("userRequestAccountId:" + accountId);
			System.out.println("authId: " + account.getId());
			if(accountId != account.getId()) {
				System.out.println("認証エラー");
				apiError.setErrorCode(403);
				return apiError;
			} else {
				return accountId;
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}
}
