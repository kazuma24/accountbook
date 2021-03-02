package com.household.accountbook.rest.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import com.household.accountbook.entity.Income;
import com.household.accountbook.entity.IncomeCategory;
import com.household.accountbook.service.IncomeService;

@SpringBootTest
@AutoConfigureMockMvc
public class IncomeMainRestControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	TestMapper tM;
	@Autowired
	IncomeService sS;
	@Autowired
	IncomeService iS;

	@BeforeAll
	static void beforeAll() {
		System.out.println("IncomeMainRestControllerTest  開始");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("IncomeMainRestControllerTest  終了");
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
	@WithMockUser("loginId")
	public void CategoryCheck() throws UnsupportedEncodingException, Exception {

		System.out.println("収入カテゴリリスト取得　初期ユーザーの場合");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ作成
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// 期待値データ作成
		List<IncomeCategory> expectedData = new ArrayList<>();
		tM.getDefaultIncomeCategory().stream().forEach((object) -> {
			IncomeCategory dumy = new IncomeCategory();
			dumy.setAccountId(id);
			dumy.setIncomeCategoryName(object.getIncomeCategoryName());
			dumy.setIncomeCategoryColor(object.getIncomeCategoryColor());
			expectedData.add(dumy);
		});
		String expected = mapper.writeValueAsString(expectedData);

		/**
		 * リクエスト(このリクエストが正常にいけば、income_categoryテーブルに
		 * default_income_categoryテーブルの情報が格納されている
		 */
		String result = mockMvc.perform(get("/incomecategorycheck")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		// テスト
		assertEquals(expected, result);

		/**
		 * もう一度リクエストしたとき
		 */
		System.out.println("収入カテゴリテーブルデータありの場合 (アクセス２回目以降のユーザー)");

		// テストのためデータを一つ格納
		IncomeCategory s = new IncomeCategory();
		s.setAccountId(id);
		s.setIncomeCategoryName("追加カテゴリ");
		s.setIncomeCategoryColor("#ffffff");
		tM.testDataIncomeRegister(s);

		// 期待値データに追加分を追加しておく
		expectedData.add(s);
		String expected1 = mapper.writeValueAsString(expectedData);

		// リクエスト(２回目)
		String result1 = mockMvc.perform(get("/incomecategorycheck")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected1, result1);

	}

	@Test
	@WithMockUser("loginId")
	public void incomeRegister() throws UnsupportedEncodingException, Exception {
		System.out.println("収入データ登録　id　不整合の場合");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Income testData = new Income();
		testData.setAccountId(4325); // 不整合
		testData.setIncomeCategoryName("給料");
		testData.setIncomeAmount(2000);
		testData.setIncomeYear(2021);
		testData.setIncomeMonth(3);
		testData.setIncomeDay(2);
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(403);
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/incomeRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void incomeRegister1() throws UnsupportedEncodingException, Exception {
		System.out.println("収入データ登録　nullの場合");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Income testData = new Income();
		testData.setAccountId(id); // 不整合
		// testData.setIncomeCategoryName("給料"); null
		testData.setIncomeAmount(2000);
		testData.setIncomeYear(2021);
		// testData.setIncomeMonth(3);
		// testData.setIncomeDay(2);
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("データに不備があり情報を取得できませんでした");
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/incomeRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void incomeRegister2() throws UnsupportedEncodingException, Exception {
		System.out.println("収入データ登録　空の場合");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Income testData = new Income();
		testData.setAccountId(id); // 不整合
		testData.setIncomeCategoryName(""); // 空
		testData.setIncomeAmount(2000);
		testData.setIncomeYear(2021);
		testData.setIncomeMonth(3);
		testData.setIncomeDay(2);
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("データに不備があり情報を取得できませんでした");
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/incomeRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void incomeRegister3() throws UnsupportedEncodingException, Exception {
		System.out.println("収入データ登録　日付データ無効の場合（年）");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Income testData = new Income();
		testData.setAccountId(id);
		testData.setIncomeCategoryName("給料");
		testData.setIncomeAmount(2000);
		testData.setIncomeYear(2321); // 不正
		testData.setIncomeMonth(2);
		testData.setIncomeDay(2);
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("日付（年）のデータが無効です。2020年から2022年まで有効です"); // 現時点では
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/incomeRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void incomeRegister4() throws UnsupportedEncodingException, Exception {
		System.out.println("収入データ登録　日付データ無効の場合(月)");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Income testData = new Income();
		testData.setAccountId(id);
		testData.setIncomeCategoryName("給料");
		testData.setIncomeAmount(2000);
		testData.setIncomeYear(2021);
		testData.setIncomeMonth(24); // 不正
		testData.setIncomeDay(2);
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("日付(月)のデータが無効です。");
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/incomeRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void incomeRegister5() throws UnsupportedEncodingException, Exception {
		System.out.println("収入データ登録　日付データ無効の場合(日)");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Income testData = new Income();
		testData.setAccountId(id);
		testData.setIncomeCategoryName("給料");
		testData.setIncomeAmount(2000);
		testData.setIncomeYear(2021);
		testData.setIncomeMonth(2);
		testData.setIncomeDay(32); // 不正
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("日付(日)のデータが無効です。");
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/incomeRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void incomeRegister6() throws UnsupportedEncodingException, Exception {
		System.out.println("収入データ登録　成功");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Income testData = new Income();
		testData.setAccountId(id);
		testData.setIncomeCategoryName("給料");
		testData.setIncomeAmount(2000);
		testData.setIncomeYear(2021);
		testData.setIncomeMonth(2);
		testData.setIncomeDay(3);
		String jsonParam = mapper.writeValueAsString(testData);

		// リクエスト
		String result = mockMvc
				.perform(post("/incomeRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		// 期待値 DBに登録してそのまま返す (DB確認OK)
		assertEquals(jsonParam, result);

	}
}
