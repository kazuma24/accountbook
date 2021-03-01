package com.household.accountbook.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.Account;
import com.household.accountbook.service.AccountService;

@Controller
public class AccountRegistrationCotroller {
	@Autowired
	AccountService accountservice;

	@GetMapping("/")
	public ModelAndView index(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) {

		// 自動ログイン機能有ユーザーの場合
		Cookie cookie[] = request.getCookies();
		if (cookie != null) {
			for (int i = 0; i < cookie.length; i++) {
				if (cookie[i].getName().equals("remember-me")) {
					// SpringSecurityからログインId取得
					String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
					if (loginId != "anonymousUser") {
						String url = "/main";
						try {
							// SpringTest OK
							response.sendRedirect(url);
						} catch (IOException e) {
							e.printStackTrace();
							mav.setViewName("error");
							mav.addObject("errorMessage", e.getMessage());
							return mav;
						}
					}
				}
			}
		}
		// SpringTest OK
		Account account = new Account();
		mav.setViewName("accountregistartion");
		mav.addObject("account", account);
		return mav;
	}

	@PostMapping("/")
	public ModelAndView accountsave(@ModelAttribute("account") Account account, ModelAndView mav) {

		// ログインID重複チェック
		String loginId = account.getLoginId();
		try {

			if (accountservice.loginIdDuplicateExamination(loginId) >= 1) {
				mav.setViewName("accountregistartion");
				mav.addObject("error", true);
				mav.addObject("errorMessage", "ログインIDが使用済です");
				return mav;
			}
		} catch (Exception e) {
			mav.setViewName("error");
			mav.addObject("errorMessage", e.getMessage());
			return mav;
		}

		// アカウント登録
		try {
			String preHashPassword = account.getPassword();
			PasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
			account.setPassword(bcryptPasswordEncoder.encode(account.getPassword()));
			accountservice.accountRegister(account);
			mav.setViewName("completionofregistration");
			mav.addObject("account", account);
			mav.addObject("preHashPassword", preHashPassword);
			return mav;
		} catch (Exception e) {
			mav.setViewName("error");
			mav.addObject("errorMessage", e.getMessage());
			return mav;
		}

	}

}
