package com.household.accountbook.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.test.web.servlet.MvcResult;

import com.household.accountbook.TestMapper;
import com.household.accountbook.entity.Account;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	TestMapper tM;

	@BeforeAll
	static void beforeAll() {
		System.out.println("LoginControllerTest 開始");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("LoginControllerTest 終了");
	}

	@BeforeEach
	void beforeEach() {
		System.out.println("====================================開始====================================");
	}

	@AfterEach
	void afterEach() {
		System.out.println("====================================終了====================================");
	}

	// ログイン画面表示テスト
	@Test
	public void loginViewTest() throws Exception {
		System.out.println("loginViewTest() /login　にGET");
		MvcResult result = mockMvc
				.perform(get("/login").param("id", "1").param("name", "testName").param("email", "test@co.jp")
						.param("loginId", "testLoginId").param("password", "testpass"))
				.andExpect(status().isOk()).andExpect(view().name("login")).andReturn();

		// 成功時レスポンスのloginFormオブジェクトに値が設定されているかチェック
		Account resultAccount = (Account) result.getModelAndView().getModel().get("loginForm");
		assertAll(() -> assertEquals("testName", resultAccount.getName()),
				() -> assertEquals("test@co.jp", resultAccount.getEmail()),
				() -> assertEquals("testLoginId", resultAccount.getLoginId()));
	}

	@Test
	@WithMockUser("testUser")
	public void logined() throws Exception {
		System.out.println("メイン画面表示コントローラテスト");
		mockMvc.perform(get("/main")).andExpect(status().isOk()).andExpect(view().name("main"));

	}
}
