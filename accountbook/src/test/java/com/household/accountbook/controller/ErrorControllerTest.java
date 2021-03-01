package com.household.accountbook.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.stream.IntStream;

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
public class ErrorControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	@Autowired
	TestMapper tM;

	
	@BeforeAll
	static void beforeAll() {
	  System.out.println("ErrorControllerTest 開始");
	}
	
	
	@AfterAll
	static void afterAll() {
	  System.out.println("ErrorControllerTest 終了");
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
	@WithMockUser("testUser")
	public void apiError() {
		System.out.println("RESTAPIでエラーが返された場合 APIエラー画面リダイレクトテスト");
		
		//テストパラメータ
		IntStream iS = IntStream.of(202,204,400,401,403,404,408,413,414,500);
		
		//各エラーコードの値でレスポンスの値をチェック
		iS.forEach((errorCode) -> {
			String testUrl = "http://localhost/api/error/" + errorCode;
			try {
				MvcResult result = mockMvc.perform(get(testUrl))
				.andExpect(status().isOk())
				.andExpect(view().name("apierror"))
				.andReturn();
				assertEquals(errorCode,result.getModelAndView().getModel().get("errorCode"));
				String errorMessageValue = (String) result.getModelAndView().getModel().get("errorMessage");
				switch(errorCode) {
				case 202:
					assertEquals("202 Accepted リクエストは受理されましたが、内部でエラーが発生しました。処理は完了していません。",errorMessageValue);
					break;
				case 204:
					assertEquals("204 No Content リクエストは受理されましたが、情報を取得できませんでした。", errorMessageValue);
					break;
				case 400:
					assertEquals("400 Bad Request 構文が無効です", errorMessageValue);
					break;
				case 401:
					assertEquals("401 Unauthorized 未認証です", errorMessageValue);
					break;
				case 403:
					assertEquals("403 Forbidden 認証情報がありません。アクセスに失敗しました。", errorMessageValue);
					break;
				case 404:
					assertEquals("404 Not found リクエストされたURLは存在しません。", errorMessageValue);
					break;
				case 408:
					assertEquals("408 Request Timeout タイムアウトしました", errorMessageValue);
					break;
				case 413:
					assertEquals("413 Payload Too Large 送信されたデータの上限を超えています", errorMessageValue);
					break;
				case 414:
					assertEquals("414 URI Too Long 扱えないURLです", errorMessageValue);
					break;
				case 500:
					assertEquals("500 Internal Server Error　サーバ内部でエラーが発生しました。", errorMessageValue);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
