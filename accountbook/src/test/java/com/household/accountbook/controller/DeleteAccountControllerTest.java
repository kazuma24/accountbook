package com.household.accountbook.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.security.test.context.support.WithMockUser;

import com.household.accountbook.TestMapper;
import com.household.accountbook.entity.Account;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteAccountControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	@Autowired
	TestMapper tM;

	
	@BeforeAll
	static void beforeAll() {
	  System.out.println("DeleteAccountControllerTest 開始");
	}
	
	
	@AfterAll
	static void afterAll() {
	  System.out.println("DeleteAccountControllerTest 終了");
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
	public void deletefromcheck() throws Exception {
		System.out.println("deletefromcheck() メイン画面から削除確認画面への遷移テスト");
		
		System.out.println("認証していないユーザーがアクセスしたときのテスト リダイレクト先 /login");
		mockMvc.perform(get("/deleteaccount"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
		
		
	}
	
	@Test
	@WithMockUser(username = "deletefromcheck1Id")
	public void deletefromcheck1() throws Exception {
		System.out.println("1 認証済ユーザーが/deleteaccountにアクセスしたときテスト　（認証ユーザーのログインIDはdeletefromcheck1Idとする）");
		
		//ログインIDがdeletefromcheck1Idのテストデータを格納
		tM.testAccountRegister("deleteUser", "testss@co.jp", "deletefromcheck1Id", "passw");
		
		//レスポンスの画面がcertification.htmｌであることをチェック　
		MvcResult result = mockMvc.perform(get("/deleteaccount"))
		.andExpect(status().isOk())
		.andExpect(view().name("certification")).andReturn();
		
		//レスポンスのaccountオブジェクト内をチェックする
		Account AccountResult = (Account) result.getModelAndView().getModel().get("account");
		assertAll(
			     () -> assertEquals("deleteUser", AccountResult.getName()),
			     () -> assertEquals("testss@co.jp", AccountResult.getEmail()),
			     () -> assertEquals("deletefromcheck1Id", AccountResult.getLoginId())
	    );
		
		
		System.out.println("2 アカウント削除確認画面から削除を実行したときのテスト　（認証ユーザーのログインIDはdeletefromcheck1Idとする）");
		//paramメソッドがStringしか受け取らないためキャスト
		String id = String.valueOf(AccountResult.getId());
		
		//削除機能実行　および　成功した場合accountregistartion.htmlが返されることをチェック
		MvcResult postResult2 = mockMvc.perform(post("/delete")
				.param("id", id)
				.param("loginId", AccountResult.getLoginId()))
				.andExpect(status().isOk())
				.andExpect(view().name("accountregistartion")).andReturn();
		
		//レスポンスのaccountオブジェクト内をチェックする
		Account postAccountResult = (Account) postResult2.getModelAndView().getModel().get("account");
		assertAll(
			     () -> assertEquals("", postAccountResult.getLoginId()),
			     () -> assertEquals(0, postAccountResult.getId())
	    );
		
		System.out.println("3 アカウント削除確認画面から削除を実行したときのテスト　（認証ユーザーのログインIDと送られてきたIdが一致しない場合）");
		MvcResult postResult3 = mockMvc.perform(post("/delete")
				.param("id", "0")
				.param("loginId", AccountResult.getLoginId()))
				.andExpect(status().isOk())
				.andExpect(view().name("apierror")).andReturn();
		int errorCode3 = (int) postResult3.getModelAndView().getModel().get("errorCode");
		String errorMessage3  = (String) postResult3.getModelAndView().getModel().get("errorMessage");
		assertAll(
			     () -> assertEquals(500, errorCode3),
			     () -> assertEquals("500 Internal Server Error　サーバ内部でエラーが発生しました。", errorMessage3)
	    );
		
		System.out.println("4 アカウント削除確認画面から削除を実行したときのテスト　（認証ユーザーのログインIdと送られてきたログインIdが一致しない場合）");
		MvcResult postResult4 = mockMvc.perform(post("/delete")
				.param("id",id)
				.param("loginId", "dummyId"))
				.andExpect(status().isOk())
				.andExpect(view().name("apierror")).andReturn();
		int errorCode4 = (int) postResult4.getModelAndView().getModel().get("errorCode");
		String errorMessage4  = (String) postResult4.getModelAndView().getModel().get("errorMessage");
		assertAll(
			     () -> assertEquals(403, errorCode4),
			     () -> assertEquals("403 Forbidden 認証情報がありません。アクセスに失敗しました。", errorMessage4)
	    );
		
		//テストデータ削除
		tM.testDataDelete("deletefromcheck1Id");
	}
	
	
	
}
