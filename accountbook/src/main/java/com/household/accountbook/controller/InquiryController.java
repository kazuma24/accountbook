package com.household.accountbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InquiryController {
	
	@GetMapping("/inquiry")
	public ModelAndView viewInquiry(ModelAndView mav) {
		mav.setViewName("inquiry");
		return mav;
	}
}
