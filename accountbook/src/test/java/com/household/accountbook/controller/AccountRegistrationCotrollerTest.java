package com.household.accountbook.controller;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import com.household.accountbook.TestMapper;
import com.household.accountbook.entity.Account;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Random;


@SpringBootTest
@AutoConfigureMockMvc
public class AccountRegistrationCotrollerTest {
	
	@Autowired
	MockMvc mockMvc;
	@Autowired
	TestMapper tM;
	
	@BeforeAll
	static void beforeAll() {
	  System.out.println("AccountRegistrationCotrollerTest 開始");
	}
	
	
	@AfterAll
	static void afterAll() {
	  System.out.println("AccountRegistrationCotrollerTest 終了");
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
	public void registerSuccses() throws Exception {
		System.out.println("registerSuccses() アカウント登録が成功した時のテスト");
		
		//ログインID重複を避けるためランダムな整数を生成 (1/100000の確率で重複する可能性あり..)
		Random rand = new Random();
	    String testNum = String.valueOf(rand.nextInt(100000) + 100);
	    String testName = "user" + testNum;
	    String testEmail = "test@" + testName + "co.jp";
		String testLoginId = "testId" + testNum;
	
		
		//アカウント登録機能　成功時completionofregistration.htmlを返しているかチェック
		MvcResult result = mockMvc.perform(post("/")
				.param("id", "1")
				.param("name", testName)
				.param("email", testEmail)
				.param("loginId", testLoginId)
				.param("password", "testpass"))
		.andExpect(status().isOk())
		.andExpect(view().name("completionofregistration")).andReturn();
		
		//成功時レスポンスのaccountオブジェクトの中身をチェック(idはDBの主キーを使っているためチェックなし、パスワードもハッシュ化のためチェックなし)
		Account resultAccount = (Account) result.getModelAndView().getModel().get("account");
		assertAll(
			     () -> assertEquals(testName, resultAccount.getName()),
			     () -> assertEquals(testEmail, resultAccount.getEmail()),
			     () -> assertEquals(testLoginId, resultAccount.getLoginId())
	    );
	 }
	
	@Test
	public void registerloginIdDuplicate() throws Exception {
		System.out.println("registerloginIdDuplicate() ログインID重複の場合のテスト");
		
		//ログインIDを重複させるためテストデータ挿入
		Random rand = new Random();
		String testNum = String.valueOf(rand.nextInt(100000) + 100);
		String DuplicateId = "loginId"+ testNum;
		tM.testAccountRegister("user1", "test1@co.jp", DuplicateId, "password1");
		
		//同じログインIDで新規登録　レスポンスがaccountregistartion.htmlかチェック
		MvcResult result = mockMvc.perform(post("/")
				.param("id", "1")
				.param("name", "user2")
				.param("email", "test2@co.jp")
				.param("loginId", DuplicateId)
				.param("password", "password2"))
		.andExpect(status().isOk())
		.andExpect(view().name("accountregistartion")).andReturn();
		
		//各オブジェクトの中身をチェック
		assertAll(
				() -> assertEquals(true, result.getModelAndView().getModel().get("error")),
				() -> assertEquals("ログインIDが使用済です", result.getModelAndView().getModel().get("errorMessage"))
		);
	}
}
