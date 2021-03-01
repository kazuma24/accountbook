package com.household.accountbook.rest.main;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.household.accountbook.TestMapper;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.ChengeCategory;
import com.household.accountbook.entity.Spending;
import com.household.accountbook.entity.SpendingCategory;
import com.household.accountbook.service.IncomeService;
import com.household.accountbook.service.SpendingService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
@SpringBootTest
@AutoConfigureMockMvc
public class SpendingEditRestControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	@Autowired
	TestMapper tM;
	@Autowired
	SpendingService sS;
	@Autowired
	IncomeService iS;
	
	@BeforeAll
	static void beforeAll() {
		System.out.println("SpendingEditRestControllerTest 開始");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("SpendingEditRestControllerTest 終了");
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
	@WithMockUser("testLoginId55")
	public void  getid() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ登録済リスト取得APIテスト URLパラメータ、id未認証の場合");
		ObjectMapper oM = new ObjectMapper();
		
		//テストアカウント
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "testLoginId55", "password");
		
		//通信
		String apierl = "/spendingcategorylist/" + 212; //212ダミー
		String result = mockMvc.perform(get(apierl))
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		//期待値
		ApiError apiError = new ApiError();
		apiError.setErrorCode(403);
		apiError.setMessage("403 Forbidden 認証情報がありません。アクセスに失敗しました。");
		
		//テスト
		String expected = oM.writeValueAsString(apiError);
		assertEquals(result, expected);
		tM.testDataDelete("testLoginId55");
		
	}
	@Test
	@WithMockUser("testLoginId66")
	public void getId1() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ登録済リスト取得APIテスト　成功の場合");	
		ObjectMapper oM = new ObjectMapper();
		//テストアカウント
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "testLoginId66", "password");
		
		//テストデータと期待値データ作成
		List<SpendingCategory> expectedData = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			SpendingCategory sC = new SpendingCategory();
			sC.setAccountId(Integer.valueOf(id));
			sC.setSpendingCategoryName("テストカテゴリ" + i);
			sC.setSpendingCategoryColor("#000000");
			expectedData.add(sC); //期待値
			tM.testDataSpendingRegister(sC); //DBへテストデータ
		}
		
		//通信
		String succsessUrl = "/spendingcategorylist/" + id;
		String result1 = mockMvc.perform(get(succsessUrl))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		//期待値Jsoｎ
		String expected1 = oM.writeValueAsString(expectedData);
		
		//テスト
		assertEquals(result1, expected1);
		tM.testDataDelete("testLoginId66");
	}
	
	@Test
	@WithMockUser("testLoginId77")
	public void addNewSpendingCategory() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ　新規登録　データ不備の場合");
		ObjectMapper oM = new ObjectMapper();
		
		//空
		String jsonParam = oM.writeValueAsString(new SpendingCategory());
		
		String result = mockMvc.perform(post("/addnewrequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonParam)).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		//期待値
		ApiError apiError = new ApiError();
		apiError.setMessage("データに不備があり情報を取得できませんでした");
		apiError.setErrorCode(400);
		String expected = oM.writeValueAsString(apiError);
		
		assertEquals(result, expected);
	}
	
	@Test
	@WithMockUser("testLoginId88")
	public void addNewSpendingCategory1() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ　新規登録　登録済カテゴリ上限の場合");
		ObjectMapper oM = new ObjectMapper();
		
		//テストアカウント
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "testLoginId88", "password");
		
		//テストパラメータ
		SpendingCategory testData = new SpendingCategory();
		testData.setAccountId(id);
		testData.setSpendingCategoryName("新規追加カテゴリ");
		testData.setSpendingCategoryColor("#ffffff");
		String jsonParam = oM.writeValueAsString(testData);
		
		//テストデータと期待値データ作成
		SpendingCategory sC = new SpendingCategory();
		for(int i = 0; i < 31; i++) {
			sC.setAccountId(Integer.valueOf(id));
			sC.setSpendingCategoryName("テストカテゴリ" + i);
			sC.setSpendingCategoryColor("#000000");
			tM.testDataSpendingRegister(sC); //DBへテストデータ
		}
		
		String result = mockMvc.perform(post("/addnewrequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonParam)).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		//期待値
		ApiError apiError = new ApiError();
		apiError.setMessage("登録できるカテゴリは30個までです");
		apiError.setErrorCode(400);
		String expected = oM.writeValueAsString(apiError);
		
		assertEquals(result, expected);
		tM.testDataDelete("testLoginId88");
	}
	@Test
	@WithMockUser("testLoginId17")
	public void addNewSpendingCategory17() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ 追加　空文字の場合");
		ObjectMapper oM = new ObjectMapper();
		
		//テストアカウント
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "testLoginId17", "password");
		
		//テストパラメータ
		SpendingCategory testData = new SpendingCategory();
		testData.setAccountId(id);
		testData.setSpendingCategoryName(""); //空文字
		testData.setSpendingCategoryColor("#ffffff");
		String jsonParam = oM.writeValueAsString(testData);

		
		//期待値データ作成
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("データに不備があり情報を取得できませんでした");
		
		String result = mockMvc.perform(post("/addnewrequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonParam)).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		//期待値
		String expected = oM.writeValueAsString(expectedData);
		
		//テスト
		assertEquals(result, expected);
		tM.testDataDelete("testLoginId14");
	}
	
	@Test
	@WithMockUser("testLoginId99")
	public void addNewSpendingCategory2() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ　新規登録　同一名のカテゴリ登録の場合");
		ObjectMapper oM = new ObjectMapper();
		
		//テストアカウント
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "testLoginId99", "password");
		
		//テストパラメータ
		SpendingCategory testData = new SpendingCategory();
		testData.setAccountId(id);
		testData.setSpendingCategoryName("テストカテゴリ1"); //かぶり
		testData.setSpendingCategoryColor("#ffffff");
		String jsonParam = oM.writeValueAsString(testData);
		
		//テストデータ作成
		SpendingCategory sC = new SpendingCategory();
		for(int i = 0; i < 3; i++) {
			sC.setAccountId(Integer.valueOf(id));
			sC.setSpendingCategoryName("テストカテゴリ" + i);
			sC.setSpendingCategoryColor("#000000");
			tM.testDataSpendingRegister(sC); //DBへテストデータ
		}
		
		String result = mockMvc.perform(post("/addnewrequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonParam)).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		//期待値
		ApiError apiError = new ApiError();
		apiError.setMessage("テストカテゴリ1は登録済です");
		apiError.setErrorCode(400);
		String expected = oM.writeValueAsString(apiError);
		
		assertEquals(result, expected);
		tM.testDataDelete("testLoginId99");
	}
	
	@Test
	@WithMockUser("testLoginId12")
	public void addNewSpendingCategory4() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ　新規登録　成功の場合");
		ObjectMapper oM = new ObjectMapper();
		
		//テストアカウント
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "testLoginId12", "password");
		
		//テストパラメータ
		SpendingCategory testData = new SpendingCategory();
		testData.setAccountId(id);
		testData.setSpendingCategoryName("新規追加カテゴリ");
		testData.setSpendingCategoryColor("#ffffff");
		String jsonParam = oM.writeValueAsString(testData);
		
		//テストデータと期待値データ作成
		List<SpendingCategory> expectedData = new ArrayList<>(); //期待値
		for(int i = 0; i < 3; i++) {
			SpendingCategory sC = new SpendingCategory();
			sC.setAccountId(Integer.valueOf(id));
			sC.setSpendingCategoryName("テストカテゴリ" + i);
			sC.setSpendingCategoryColor("#000000");
			expectedData.add(sC); //期待値
			tM.testDataSpendingRegister(sC); //DBへテストデータ
		}
		//期待値　追加したカテゴリ
		expectedData.add(testData);
		
		String result = mockMvc.perform(post("/addnewrequest")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonParam)).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		//期待値
		String expected = oM.writeValueAsString(expectedData);
		
		//テスト
		assertEquals(result, expected);
		tM.testDataDelete("testLoginId12");
	}
	
	@Test
	@WithMockUser("testLoginId13")
	public void changeSpendingCategory() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ 編集　データ不備");
		ObjectMapper oM = new ObjectMapper();
		
		//テストアカウント
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "testLoginId13", "password");
		
		//テストパラメータ
		ChengeCategory testData = new ChengeCategory();
		testData.setAccountId(id);
		testData.setBeforeName("変更前");
		String jsonParam = oM.writeValueAsString(testData);
		
		
		//期待値データ作成
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("データに不備があり情報を取得できませんでした");
		
		String result = mockMvc.perform(post("/changecategory")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonParam)).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		//期待値
		String expected = oM.writeValueAsString(expectedData);
		
		//テスト
		assertEquals(result, expected);
		tM.testDataDelete("testLoginId13");
	}
	
	@Test
	@WithMockUser("testLoginId14")
	public void changeSpendingCategory1() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ 編集　di不整合の場合");
		ObjectMapper oM = new ObjectMapper();
		
		//テストアカウント
		tM.testAccountRegisterGetId("test", "test@co.jp", "testLoginId14", "password");
		
		//テストパラメータ
		ChengeCategory testData = new ChengeCategory();
		testData.setAccountId(999); //不整合の値
		testData.setBeforeName("変更前");
		testData.setAfterName("変更後");
		testData.setAfterColor("#fff000");
		String jsonParam = oM.writeValueAsString(testData);
		
		
		//期待値データ作成
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(403);
		
		String result = mockMvc.perform(post("/changecategory")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonParam)).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		//期待値
		String expected = oM.writeValueAsString(expectedData);
		
		//テスト
		assertEquals(result, expected);
		tM.testDataDelete("testLoginId14");
	}
	
	@Test
	@WithMockUser("testLoginId15")
	public void changeSpendingCategory2() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ 編集　空文字の場合");
		ObjectMapper oM = new ObjectMapper();
		
		//テストアカウント
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "testLoginId15", "password");
		
		//テストパラメータ
		ChengeCategory testData = new ChengeCategory();
		testData.setAccountId(id);
		testData.setBeforeName(""); //空文字
		testData.setAfterName("変更後");
		testData.setAfterColor("#fff000");
		String jsonParam = oM.writeValueAsString(testData);
		
		
		//期待値データ作成
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("データに不備があり情報を取得できませんでした");
		
		String result = mockMvc.perform(post("/changecategory")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonParam)).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		//期待値
		String expected = oM.writeValueAsString(expectedData);
		
		//テスト
		assertEquals(result, expected);
		tM.testDataDelete("testLoginId15");
	}
	
	@Test
	@WithMockUser("testLoginId23")
	public void changeSpendingCategory23() throws UnsupportedEncodingException, Exception {
		System.out.println("支出カテゴリ 編集　成功の場合");
		ObjectMapper oM = new ObjectMapper();
		
		//テストアカウント
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "testLoginId23", "password");
		
		//テストパラメータ
		ChengeCategory testData = new ChengeCategory();
		testData.setAccountId(id);
		testData.setBeforeName("変更前");
		testData.setAfterName("変更後");
		testData.setAfterColor("#fff000");
		String jsonParam = oM.writeValueAsString(testData);
		
		//テストデータと期待値データ作成
		List<SpendingCategory> expectedData = new ArrayList<>(); //期待値
		for(int i = 0; i < 2; i++) {
			SpendingCategory sC = new SpendingCategory();
			if(i == 0) {
				//「変更前」テストデータ作成しておく
				sC.setAccountId(Integer.valueOf(id));
				sC.setSpendingCategoryName("変更前");
				sC.setSpendingCategoryColor("#000000");
				tM.testDataSpendingRegister(sC);
				
			}
			sC.setAccountId(Integer.valueOf(id));
			sC.setSpendingCategoryName("テストカテゴリ" + i);
			sC.setSpendingCategoryColor("#000000");
			expectedData.add(sC); //期待値
			tM.testDataSpendingRegister(sC); //DBへテストデータ
		}
		//期待値データ　変更分生成
		SpendingCategory after = new SpendingCategory();
		after.setAccountId(id);
		after.setSpendingCategoryName("変更後");
		after.setSpendingCategoryColor("#fff000");
		expectedData.add(after);
		
		String result = mockMvc.perform(post("/changecategory")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonParam)).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		//期待値
		String expected = oM.writeValueAsString(expectedData);
		
		//テスト
		assertEquals(result, expected);
		tM.testDataDelete("testLoginId23");
	}
	
	
}
