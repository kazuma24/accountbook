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
import com.household.accountbook.entity.Spending;
import com.household.accountbook.entity.SpendingCategory;
import com.household.accountbook.service.IncomeService;
import com.household.accountbook.service.SpendingService;

@SpringBootTest
@AutoConfigureMockMvc
public class SpendingMainRestControllerTest {
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
		System.out.println("SpendingMainRestControllerTest  開始");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("SpendingMainRestControllerTest  終了");
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

		System.out.println("支出カテゴリリスト取得　初期ユーザーの場合");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ作成
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// 期待値データ作成
		List<SpendingCategory> expectedData = new ArrayList<>();
		tM.getDefaultSpendingCategory().stream().forEach((object) -> {
			SpendingCategory dumy = new SpendingCategory();
			dumy.setAccountId(id);
			dumy.setSpendingCategoryName(object.getSpendingCategoryName());
			dumy.setSpendingCategoryColor(object.getSpendingCategoryColor());
			expectedData.add(dumy);
		});
		String expected = mapper.writeValueAsString(expectedData);

		/**
		 * リクエスト(このリクエストが正常にいけば、spending_categoryテーブルに
		 * default_spending_categoryテーブルの情報が格納されている
		 */
		String result = mockMvc.perform(get("/categorycheck")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		// テスト
		assertEquals(expected, result);

		/**
		 * もう一度リクエストしたとき
		 */
		System.out.println("支出カテゴリテーブルデータありの場合 (アクセス２回目以降のユーザー)");

		// テストのためデータを一つ格納
		SpendingCategory s = new SpendingCategory();
		s.setAccountId(id);
		s.setSpendingCategoryName("追加カテゴリ");
		s.setSpendingCategoryColor("#ffffff");
		tM.testDataSpendingRegister(s);

		// 期待値データに追加分を追加しておく
		expectedData.add(s);
		String expected1 = mapper.writeValueAsString(expectedData);

		// リクエスト(２回目)
		String result1 = mockMvc.perform(get("/categorycheck")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected1, result1);

	}

	@Test
	@WithMockUser("loginId")
	public void spendingRegister() throws UnsupportedEncodingException, Exception {
		System.out.println("支出データ登録　id　不整合の場合");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Spending testData = new Spending();
		testData.setAccountId(4325); // 不整合
		testData.setSpendingCategoryName("食費");
		testData.setSpendingAmount(2000);
		testData.setSpendingYear(2021);
		testData.setSpendingMonth(3);
		testData.setSpendingDay(2);
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(403);
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/spendingRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void spendingRegister1() throws UnsupportedEncodingException, Exception {
		System.out.println("支出データ登録　nullの場合");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Spending testData = new Spending();
		testData.setAccountId(id); // 不整合
		// testData.setSpendingCategoryName("食費"); null
		testData.setSpendingAmount(2000);
		testData.setSpendingYear(2021);
		// testData.setSpendingMonth(3);
		// testData.setSpendingDay(2);
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("データに不備があり情報を取得できませんでした");
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/spendingRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void spendingRegister2() throws UnsupportedEncodingException, Exception {
		System.out.println("支出データ登録　空の場合");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Spending testData = new Spending();
		testData.setAccountId(id); // 不整合
		testData.setSpendingCategoryName(""); // 空
		testData.setSpendingAmount(2000);
		testData.setSpendingYear(2021);
		testData.setSpendingMonth(3);
		testData.setSpendingDay(2);
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("データに不備があり情報を取得できませんでした");
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/spendingRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void spendingRegister3() throws UnsupportedEncodingException, Exception {
		System.out.println("支出データ登録　日付データ無効の場合（年）");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Spending testData = new Spending();
		testData.setAccountId(id);
		testData.setSpendingCategoryName("食費");
		testData.setSpendingAmount(2000);
		testData.setSpendingYear(2321); // 不正
		testData.setSpendingMonth(2);
		testData.setSpendingDay(2);
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("日付（年）のデータが無効です。2020年から2022年まで有効です"); // 現時点では
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/spendingRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void spendingRegister4() throws UnsupportedEncodingException, Exception {
		System.out.println("支出データ登録　日付データ無効の場合(月)");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Spending testData = new Spending();
		testData.setAccountId(id);
		testData.setSpendingCategoryName("食費");
		testData.setSpendingAmount(2000);
		testData.setSpendingYear(2021);
		testData.setSpendingMonth(24); // 不正
		testData.setSpendingDay(2);
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("日付(月)のデータが無効です。");
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/spendingRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void spendingRegister5() throws UnsupportedEncodingException, Exception {
		System.out.println("支出データ登録　日付データ無効の場合(日)");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Spending testData = new Spending();
		testData.setAccountId(id);
		testData.setSpendingCategoryName("食費");
		testData.setSpendingAmount(2000);
		testData.setSpendingYear(2021);
		testData.setSpendingMonth(2);
		testData.setSpendingDay(32); // 不正
		String jsonParam = mapper.writeValueAsString(testData);

		// 期待値
		ApiError expectedData = new ApiError();
		expectedData.setErrorCode(400);
		expectedData.setMessage("日付(日)のデータが無効です。");
		String expected = mapper.writeValueAsString(expectedData);

		// リクエスト
		String result = mockMvc
				.perform(post("/spendingRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		assertEquals(expected, result);

	}

	@Test
	@WithMockUser("loginId")
	public void spendingRegister6() throws UnsupportedEncodingException, Exception {
		System.out.println("支出データ登録　成功");
		ObjectMapper mapper = new ObjectMapper();

		// テストデータ
		int id = tM.testAccountRegisterGetId("test", "test@co.jp", "loginId", "password");

		// パラメータ
		Spending testData = new Spending();
		testData.setAccountId(id);
		testData.setSpendingCategoryName("食費");
		testData.setSpendingAmount(2000);
		testData.setSpendingYear(2021);
		testData.setSpendingMonth(2);
		testData.setSpendingDay(3);
		String jsonParam = mapper.writeValueAsString(testData);

		// リクエスト
		String result = mockMvc
				.perform(post("/spendingRegisterRequest").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// テストデータ削除
		tM.testDataDelete("loginId");

		// 期待値 DBに登録してそのまま返す (DB確認OK)
		assertEquals(jsonParam, result);

	}
}
