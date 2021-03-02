package com.household.accountbook.rest.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

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
import com.household.accountbook.entity.YearReport;
import com.household.accountbook.entity.YearTotal;
import com.household.accountbook.service.IncomeService;
import com.household.accountbook.service.SpendingService;

@SpringBootTest
@AutoConfigureMockMvc
public class YearReportrestControllerTest {
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
		System.out.println("YearReportrestControllerTest 開始");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("YearReportrestControllerTest 終了");
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
	public void getMonthryReport() throws UnsupportedEncodingException, Exception {
		System.out.println("年間レポート取得　nullの場合");

		// テストデータ作成
		int id = tM.testAccountRegisterGetId("test", "fadfs@co.jp", "loginId", "fsds");
		ObjectMapper mapper = new ObjectMapper();

		// パラメータ作成 パラメータ不足
		YearReport testData = new YearReport();
		testData.setAccountId(id);

		// 期待値作成
		ApiError apiError = new ApiError();
		apiError.setMessage("データに不備があり情報を取得できませんでした");
		apiError.setErrorCode(400);

		// ｊsonへ変換
		String json = mapper.writeValueAsString(testData);
		String expected = mapper.writeValueAsString(apiError);

		// テスト実施
		String result = mockMvc.perform(post("/yeargettotal").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		tM.testDataDelete("loginId");
		assertEquals(expected, result);
	}

	@Test
	@WithMockUser("loginId")
	public void getMonthryReport1() throws UnsupportedEncodingException, Exception {
		System.out.println("年間レポート取得　空の場合");

		// テストデータ作成
		int id = tM.testAccountRegisterGetId("test", "fadfs@co.jp", "loginId", "fsds");
		ObjectMapper mapper = new ObjectMapper();

		// パラメータ作成
		YearReport testData = new YearReport();
		testData.setAccountId(id);
		testData.setLoginId(""); // 空s
		testData.setYear(2021);

		// 期待値作成
		ApiError apiError = new ApiError();
		apiError.setMessage("データに不備があり情報を取得できませんでした");
		apiError.setErrorCode(400);

		// ｊsonへ変換
		String json = mapper.writeValueAsString(testData);
		String expected = mapper.writeValueAsString(apiError);

		// テスト実施
		String result = mockMvc.perform(post("/yeargettotal").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		tM.testDataDelete("loginId");
		assertEquals(expected, result);
	}

	@Test
	@WithMockUser("loginId")
	public void getMonthryReport2() throws UnsupportedEncodingException, Exception {
		System.out.println("年間レポート取得　id不整合の場合");

		// テストデータ作成
		tM.testAccountRegisterGetId("test", "fadfs@co.jp", "loginId", "fsds");
		ObjectMapper mapper = new ObjectMapper();

		// パラメータ作成
		YearReport testData = new YearReport();
		testData.setAccountId(4223); // 不整合
		testData.setLoginId("loginId");
		testData.setYear(2021);

		// 期待値作成
		ApiError apiError = new ApiError();
		apiError.setErrorCode(403);

		// ｊsonへ変換
		String json = mapper.writeValueAsString(testData);
		String expected = mapper.writeValueAsString(apiError);

		// テスト実施
		String result = mockMvc.perform(post("/yeargettotal").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		tM.testDataDelete("loginId");
		assertEquals(expected, result);
	}

	@Test
	@WithMockUser("loginId")
	public void getMonthryReport3() throws UnsupportedEncodingException, Exception {
		System.out.println("年間レポート取得　成功の場合");

		// テストデータ作成
		int id = tM.testAccountRegisterGetId("test", "fadfs@co.jp", "loginId", "fsds");
		ObjectMapper mapper = new ObjectMapper();
		tM.testDataSpendingRegister(null);

		// パラメータ作成
		YearReport testData = new YearReport();
		testData.setAccountId(id);
		testData.setLoginId("loginId");
		testData.setYear(2021);

		// 期待値作成 （データないため全て0になる）
		YearTotal expectedData = new YearTotal();
		expectedData.setAccountId(id);
		expectedData.setTotalYearSpending(0);
		expectedData.setTotalYearIncome(0);
		expectedData.setYearBalance(0);

		// ｊsonへ変換
		String json = mapper.writeValueAsString(testData);
		String expected = mapper.writeValueAsString(expectedData);

		// テスト実施
		String result = mockMvc.perform(post("/yeargettotal").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		tM.testDataDelete("loginId");
		assertEquals(expected, result);
	}
}
