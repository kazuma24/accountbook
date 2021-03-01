package com.household.accountbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.servlet.ModelAndView;

import com.household.accountbook.error.ErrorMessages;

@Controller
public class ErrorController {

	// APIエラーリダイレクト用
	@GetMapping("/api/error/{errorCode}")
	public ModelAndView apiError(@PathVariable int errorCode, ModelAndView mav) {
		// SpringTest OK
		System.out.println(" ErrorController: apiError()");
		mav.setViewName("apierror");
		mav.addObject("errorCode", errorCode);
		String strErrorCode = String.valueOf(errorCode);
		if (strErrorCode.equals("202")) {
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_202);
		} else if (strErrorCode.equals("204")) {
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_204);
		} else if (strErrorCode.equals("400")) {
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_400);
		} else if (strErrorCode.equals("401")) {
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_401);
		} else if (strErrorCode.equals("403")) {
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_403);
		} else if (strErrorCode.equals("404")) {
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_404);
		} else if (strErrorCode.equals("408")) {
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_408);
		} else if (strErrorCode.equals("413")) {
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_413);
		} else if (strErrorCode.equals("414")) {
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_414);
		} else {
			mav.addObject("errorMessage", ErrorMessages.ERRORMESSAGE_500);
		}
		return mav;
	}
}
