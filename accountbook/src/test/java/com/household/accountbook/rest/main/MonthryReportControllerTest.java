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
import com.household.accountbook.entity.Income;
import com.household.accountbook.entity.MonthryReport;
import com.household.accountbook.entity.MonthryTotal;
import com.household.accountbook.entity.Spending;
import com.household.accountbook.service.IncomeService;
import com.household.accountbook.service.SpendingService;

@SpringBootTest
@AutoConfigureMockMvc
public class MonthryReportControllerTest {

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
		System.out.println("MonthryReportControllerTest 開始");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("MonthryReportControllerTest 終了");
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
	@WithMockUser("abcd12")
	public void getMonthryReport() throws UnsupportedEncodingException, Exception {
		System.out.println("データに不備がありリクエストした場合");

		// テストデータ作成
		int id = tM.testAccountRegisterGetId("test", "fadfs@co.jp", "abcd12", "fsds");
		ObjectMapper mapper = new ObjectMapper();

		// パラメータ作成
		MonthryReport Data = new MonthryReport();
		Data.setAccountId(id);
		Data.setLoginId("abcd12");
		Data.setYear(2021);

		// 期待値作成
		ApiError apiError = new ApiError();
		apiError.setMessage("データに不備があり情報を取得できませんでした");
		apiError.setErrorCode(400);

		// ｊsonへ変換
		String json = mapper.writeValueAsString(Data);
		String result = mapper.writeValueAsString(apiError);

		// テスト実施
		String resJsonString = mockMvc.perform(post("/gettotal").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("res" + resJsonString);
		tM.testDataDelete("abcd12");
		assertEquals(resJsonString, result);
	}

	@Test
	@WithMockUser("testLoginId33")
	public void getMonthryReport1() throws UnsupportedEncodingException, Exception {
		System.out.println("リクエストのidが認証済のデータと違う場合");
		
		//テストデータ作成
		tM.testAccountRegisterGetId("test", "fadfwqs@co.jp", "testLoginId33", "fsds");
		ObjectMapper mapper = new ObjectMapper();
		
		//パラメータ作成
		MonthryReport testdata = new MonthryReport();
		testdata.setAccountId(22); //Id相違
		testdata.setYear(2021);
		testdata.setMonth(12);
		
		//期待値
		ApiError apiError2 = new ApiError();
		apiError2.setErrorCode(403);
		
		//json変換
		String json = mapper.writeValueAsString(testdata);
		String result = mapper.writeValueAsString(apiError2);
		
		//リクエスト
		String resJsonString = mockMvc.perform(post("/gettotal")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		tM.testDataDelete("testLoginId33");
		assertEquals(resJsonString, result);
	}
	
	@Test
	@WithMockUser("testLoginId34")
	public void getMonthryReport2() throws UnsupportedEncodingException, Exception {
		System.out.println("指定月の支出及び収入金額を取得するテスト");
		
		ObjectMapper mapper = new ObjectMapper();
		
		// テストデータ作成
		int accountId = tM.testAccountRegisterGetId("teczst", "fadcfs@co.jp", "testLoginId34", "fsds");
		
		Spending spending = new Spending();
		spending.setAccountId(accountId);
		spending.setSpendingCategoryName("食費");
		spending.setSpendingAmount(2000);
		spending.setSpendingYear(2021);
		spending.setSpendingMonth(3);
		spending.setSpendingDay(1);
		
		Income income = new Income();
		income.setAccountId(accountId);
		income.setIncomeCategoryName("給与");
		income.setIncomeAmount(200000);
		income.setIncomeYear(2021);
		income.setIncomeMonth(3);
		income.setIncomeDay(3);
		

		sS.spendingRegister(spending);

		iS.incomeRegister(income);
		
		//パラメータ作成
		MonthryReport testdata = new MonthryReport();
		testdata.setAccountId(accountId); 
		testdata.setYear(2021);
		testdata.setMonth(3);
		
		//期待値
		MonthryTotal monthrytotal = new MonthryTotal();
		monthrytotal.setAccountId(accountId);
		monthrytotal.setTotalMonthlySpending(2000);
		monthrytotal.setTotalMonthryIncome(200000);
		monthrytotal.setMonthlyBalance(198000);
		
		String json = mapper.writeValueAsString(testdata);
		String result = mapper.writeValueAsString(monthrytotal);
		
		//リクエスト
		String resJsonString = mockMvc.perform(post("/gettotal")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		
		tM.testDataDelete("testLoginId34");
		assertEquals(resJsonString, result);
		
	}

}
