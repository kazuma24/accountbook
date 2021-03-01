package com.household.accountbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.household.accountbook.entity.Account;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@ModelAttribute Account account, ModelAndView mav) {
		// SpringTest OK
		mav.setViewName("login");
		mav.addObject("loginForm", account);
		return mav;
	}

	/**
	 * メインページに遷移する。 ログインが成功した場合、このメソッドが呼び出される。
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String logined() {
		System.out.println("LoginController: logined()");
		return "main";
	}

}
