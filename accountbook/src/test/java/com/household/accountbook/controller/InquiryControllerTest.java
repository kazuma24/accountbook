package com.household.accountbook.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.household.accountbook.TestMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class InquiryControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	TestMapper tM;

	
	@BeforeAll
	static void beforeAll() {
	  System.out.println("InquiryControllerTest 開始");
	}
	
	
	@AfterAll
	static void afterAll() {
	  System.out.println("InquiryControllerTest 終了");
	}
	
	@BeforeEach
	void beforeEach() {
	  System.out.println("====================================開始====================================");
	}
	
	  
	@AfterEach
	void afterEach() {
	  System.out.println("====================================終了====================================");
	}
	
	@Test
	public void viewInquiry1() throws Exception {
		System.out.println("認証されていないユーザーがアクセスした場合");
		
		// 302　ログイン画面へリダイレクト
		mockMvc.perform(get("/inquiry"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
		
	}
	
	@Test
	@WithMockUser("testUser")
	public void viewInquiry2() throws Exception {
		System.out.println("認証済ユーザーがアクセスした場合 inquiry.htmlが返却される");
		
		mockMvc.perform(get("/inquiry"))
		.andExpect(status().isOk())
		.andExpect(view().name("inquiry"));
	}
	
}
