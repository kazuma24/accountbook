package com.household.accountbook.rest.main;

import java.util.Optional;
import java.util.logging.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.Account;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.service.AccountService;

@RestController
public class ReportRestController {
	
	@Autowired
	ApiError apiError;
	
	@Autowired
	AccountService accountService;
	
	Logger logger = Logger.getLogger( ReportRestController.class.getName());
	
	//ID取得（月間/年間）
	@RequestMapping("/getid")
	public Object getid() {
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		logger.info("ログインID" + loginId);
		
		if(loginId.equals("anonymousUser")) {
			//ログイン認証情報がanonymousUserの場合
			logger.info("認証情報なし" + loginId);
			apiError.setErrorCode(403);
			return apiError;
		}
		try {
			Optional<Account> opAccount = Optional.ofNullable(accountService.loginExamination(loginId));
			if(!opAccount.isPresent()) {
				logger.info("認証情報なし" + loginId);
				apiError.setErrorCode(403);
				return apiError;
			} else {
				return opAccount.get();
			}
		} catch(Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
			
		}
	}
}
