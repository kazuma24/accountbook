package com.household.accountbook.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

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

@SpringBootTest
@AutoConfigureMockMvc
public class IdCheckFromToMainControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	@Autowired
	TestMapper tM;

	
	@BeforeAll
	static void beforeAll() {
	  System.out.println("IdCheckFromToMainControllerTest 開始");
	}
	
	
	@AfterAll
	static void afterAll() {
	  System.out.println("IdCheckFromToMainControllerTest 終了");
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
	@WithMockUser("testLoginId11")
	public void idCheckToMove() throws Exception {
		System.out.println("メイン画面から支出or収入カテゴリ編集画面への遷移テスト");
		
		//テストデータ挿入後 serial（主キー）取得
		int testId = tM.testAccountRegisterGetId("user", "user@co.jp", "testLoginId11", "dsafnems");
		
		System.out.println("1-1 認証済ユーザのアクセスの場合(正式な自分のID)　main.htmlが返却される（支出）");
		String sUrl = "/spendingedit/" + testId;
		mockMvc.perform(get(sUrl))
				.andExpect(status().isOk())
				.andExpect(view().name("main"));
		
		System.out.println("1-2 認証済ユーザのアクセスの場合(正式な自分のID)　main.htmlが返却される(収入)");
		String iUrl = "/incomeedit/" + testId;
		mockMvc.perform(get(iUrl))
				.andExpect(status().isOk())
				.andExpect(view().name("main"));
		
		System.out.println("2-1 自分のId以外にアクセスした場合　apierror.html で403表示(支出)");
		MvcResult result2 = mockMvc.perform(get("/spendingedit/0"))
				.andExpect(status().isOk())
				.andExpect(view().name("apierror"))
				.andReturn();
		//レスポンスの中身チェック
		int errorCode = (int) result2.getModelAndView().getModel().get("errorCode");
		String errorMessage = (String) result2.getModelAndView().getModel().get("errorMessage");
		assertAll(
			     () -> assertEquals(403, errorCode),
			     () -> assertEquals("403 Forbidden 認証情報がありません。アクセスに失敗しました。", errorMessage)
	    );
		
		System.out.println("2-2 自分のId以外にアクセスした場合　apierror.html で403表示(収入)");
		MvcResult result3 = mockMvc.perform(get("/incomeedit/0"))
				.andExpect(status().isOk())
				.andExpect(view().name("apierror"))
				.andReturn();
		//レスポンスの中身チェック
		int errorCode3 = (int) result3.getModelAndView().getModel().get("errorCode");
		String errorMessage3 = (String) result3.getModelAndView().getModel().get("errorMessage");
		assertAll(
			     () -> assertEquals(403, errorCode3),
			     () -> assertEquals("403 Forbidden 認証情報がありません。アクセスに失敗しました。", errorMessage3)
	    );
		tM.testDataDelete("testLoginId11");
		
	}
	
	@Test
	@WithMockUser("testUser")
	public void ratioFromMain() throws Exception {
		System.out.println("メイン画面(レポート)から割合画面への遷移テスト");
		
		//テストパラメータ
		LocalDate localDate = LocalDate.now();
		int currentYear = localDate.getYear();
		int currentMonth = localDate.getMonthValue();
		
		System.out.println("1-1 認証済ユーザーで、正当な日時のURLにアクセスした場合　成功、main.html返却 (支出)");
		String StestUrl = "/monthryspendingratio/" + currentYear + "/" + currentMonth;
		mockMvc.perform(get(StestUrl))
				.andExpect(status().isOk())
				.andExpect(view().name("main"));
		
		System.out.println("1-2 認証済ユーザーで、正当な日時のURLにアクセスした場合　成功、main.html返却 (収入)");
		String ItestUrl = "/monthryincomeratio/" + currentYear + "/" + currentMonth;
		mockMvc.perform(get(ItestUrl))
				.andExpect(status().isOk())
				.andExpect(view().name("main"));
		
		System.out.println("2-1 認証済ユーザーで、不正な日時のURLにアクセスした場合　年(支出)");
		String S21testUrl = "/monthryspendingratio/" + "3283" + "/" + currentMonth;
		MvcResult S21R = mockMvc.perform(get(S21testUrl))
				.andExpect(status().isOk())
				.andExpect(view().name("apierror")).andReturn();
		assertAll(
			     () -> assertEquals(404, S21R.getModelAndView().getModel().get("errorCode")),
			     () -> assertEquals("404 Not found リクエストされたURLは存在しません。", S21R.getModelAndView().getModel().get("errorMessage")
	    ));
		
		System.out.println("2-2 認証済ユーザーで、不正な日時のURLにアクセスした場合　月(支出)");
		String S22testUrl = "/monthryspendingratio/" + currentYear + "/" + "85";
		MvcResult S22R = mockMvc.perform(get(S22testUrl))
				.andExpect(status().isOk())
				.andExpect(view().name("apierror")).andReturn();
		assertAll(
			     () -> assertEquals(404, S22R.getModelAndView().getModel().get("errorCode")),
			     () -> assertEquals("404 Not found リクエストされたURLは存在しません。", S22R.getModelAndView().getModel().get("errorMessage")
	    ));
		
		System.out.println("2-3 認証済ユーザーで、不正な日時のURLにアクセスした場合　年(収入)");
		String I23testUrl = "/monthryincomeratio/" + "3283" + "/" + currentMonth;
		MvcResult I23R = mockMvc.perform(get(I23testUrl))
				.andExpect(status().isOk())
				.andExpect(view().name("apierror")).andReturn();
		assertAll(
			     () -> assertEquals(404, I23R.getModelAndView().getModel().get("errorCode")),
			     () -> assertEquals("404 Not found リクエストされたURLは存在しません。", I23R.getModelAndView().getModel().get("errorMessage")
	    ));
		
		System.out.println("2-4 認証済ユーザーで、不正な日時のURLにアクセスした場合　月(収入)");
		String I24testUrl = "/monthryincomeratio/" + currentYear + "/" + "85";
		MvcResult I24R = mockMvc.perform(get(I24testUrl))
				.andExpect(status().isOk())
				.andExpect(view().name("apierror")).andReturn();
		assertAll(
			     () -> assertEquals(404, I24R.getModelAndView().getModel().get("errorCode")),
			     () -> assertEquals("404 Not found リクエストされたURLは存在しません。", I24R.getModelAndView().getModel().get("errorMessage")
	    ));
		
		
	}
}
