package com.household.accountbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.Account;
import com.household.accountbook.error.ErrorMessages;
import com.household.accountbook.service.AccountService;

@Controller
public class DeleteAccountController {
	
	@Autowired
	AccountService accountService;
	
	@GetMapping("/deleteaccount")
	public ModelAndView deletefromcheck(ModelAndView mav) {
		//認証情報からログインId取得
	    String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		if(loginId.equals("anonymousUser")) {
			//ログイン認証情報がanonymousUserの場合
			mav.setViewName("apierror");
			mav.addObject("errorCode",403);
			mav.addObject("errorMessage",ErrorMessages.ERRORMESSAGE_403);
			return mav;
		}
		try {
			//SpringTest OK
			Account account = accountService.loginExamination(loginId);
			mav.setViewName("certification");
			mav.addObject("account", account);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("apierror");
			mav.addObject("errorCode",500);
			mav.addObject("errorMessage",ErrorMessages.ERRORMESSAGE_500);
			return mav;
		}
	}
	
	@PostMapping("/delete")
	public ModelAndView delete(@ModelAttribute("account") Account account, ModelAndView mav) {
		//認証情報からログインId取得
	    String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		if(loginId.equals("anonymousUser")) {
			//ログイン認証情報がanonymousUserの場合
			mav.setViewName("apierror");
			mav.addObject("errorCode",403);
			mav.addObject("errorMessage",ErrorMessages.ERRORMESSAGE_403);
			return mav;
		}
		//認証情報と送られてきたログインIDが一致するか確認
		if(loginId.equals(account.getLoginId())) {
			try {
				//SpringTest OK
				int accountId = (int) accountService.idCollationCheck(account.getId(), loginId);;
				accountService.allDelete(accountId);
				account.setLoginId("");
				account.setId(0);
				mav.addObject("account",account);
				System.out.println("account Delete OK");
				mav.setViewName("accountregistartion");
				return mav;
			} catch (Exception e) {
				//SpringTest OK
				mav.setViewName("apierror");
				mav.addObject("errorCode",500);
				mav.addObject("errorMessage",ErrorMessages.ERRORMESSAGE_500);
				return mav;
			}
		} else {
			//ログイン認証情報がanonymousUserの場合
			//SpringTest OK
			mav.setViewName("apierror");
			mav.addObject("errorCode",403);
			mav.addObject("errorMessage",ErrorMessages.ERRORMESSAGE_403);
			return mav;
		}
		
		
	}
}
