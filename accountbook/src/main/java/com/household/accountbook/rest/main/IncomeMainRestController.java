package com.household.accountbook.rest.main;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.IncomeCategory;
import com.household.accountbook.error.ErrorMessages;
import com.household.accountbook.entity.Income;
import com.household.accountbook.service.AccountService;
import com.household.accountbook.service.IncomeCategoryService;
import com.household.accountbook.service.IncomeService;


@RestController
public class IncomeMainRestController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	IncomeCategoryService incomeCategoryService;
	
	@Autowired
	IncomeService incomeService;
	
	@Autowired
	ApiError apiError;
	
	@Autowired
	AuthenticationInformation authenticationInformation;
	

	Logger logger = Logger.getLogger(IncomeMainRestController.class.getName());
			
	@RequestMapping("/incomecategorycheck")
	public Object CategoryCheck() {
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		logger.info("ログインID" + loginId);
		if(loginId.equals("anonymousUser")) {
			//ログイン認証情報がanonymousUserの場合
			apiError.setErrorCode(403);
			return apiError;
		}
		try {
			List<IncomeCategory> incomeCategory =  incomeCategoryResourceAcquisition(loginId);
			return incomeCategory;
		} catch (SQLException e) {
			//SQLエラーの場合
			logger.info("incomeCategoryResourceAcquisition():SQLException" + e.getCause());
			apiError.setErrorCode(202);
			return apiError;
		} catch (Exception e) {
			//何らかのエラーの場合
			logger.info("incomeCategoryResourceAcquisition():Exception" + e.getCause());
			apiError.setErrorCode(204);
			return apiError;
		}

    }
	
	@Transactional
	public List<IncomeCategory> incomeCategoryResourceAcquisition(String loginId) throws Exception, SQLException {
		try {
			Optional<Object> OptionalId = Optional.ofNullable(accountService.incomeCategoryCheckAndIdAcquisition(loginId));
			if(OptionalId.isPresent()) {
				//デフォルトアカウント（初期ユーザー）
				int id = (int) OptionalId.get();
				logger.info("income_categoryに登録なし。デフォルトの収入カテゴリを設定し、設定したカテゴリとカラーを取得する");
				List<IncomeCategory> incomeCategory = incomeCategoryService.DefaultCategorySettingAndGet(id);
				return incomeCategory;
			} else {
				//収入カテゴリテーブルデータありの場合
				logger.info("income_categoryに登録あり。登録済の収入カテゴリを取得しする");
				List<IncomeCategory> incomeCategory = incomeCategoryService.SetIncomeCategory(loginId);
				return incomeCategory;
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@RequestMapping("/incomeRegisterRequest")
	public Object incomeRegister(@RequestBody Income income) {
		logger.info("incomeRegister(): body " + income);
		System.out.println("incomeRegister(): body " + income);
		try {
			//認証情報からログインID
			String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
			//ID照合チェック
			Object obj = authenticationInformation.iDVerificationcheck(loginId, income.getAccountId());
			if(obj instanceof ApiError) {
				return apiError = (ApiError) obj;
			}
			//null 空文字　チェック
			List<Object> requestParams = Arrays.asList(
					income.getAccountId(), income.getIncomeAmount(),
					income.getIncomeCategoryName(), income.getIncomeYear(),
					income.getIncomeMonth(), income.getIncomeDay());
			System.out.println("フィルタ前: ");
			requestParams.stream().forEach(System.out::println);
			Long check = requestParams
					.stream()
					.filter(s -> s == null || s.equals("") || s.equals(0))
					.count();
			System.out.println("Check: " +  check);
			if(check > 0) {
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}
			//有効な日付データかチェック
			LocalDate date = LocalDate.now();
			int currentYear = date.getYear();
			

			int year = income.getIncomeYear();
			int month = income.getIncomeMonth();
			int day = income.getIncomeDay();
			
			//現在年～前年かチェック
			if(year == currentYear || year == (currentYear -1) || year == (currentYear + 1)) {
				System.out.println("ok" + year);
			} else {
				String lastYear = String.valueOf((currentYear -1));
				String nextYear = String.valueOf((currentYear + 1));
				apiError.setMessage("日付（年）のデータが無効です。" + lastYear + "年から" + nextYear + "年まで有効です");
				return apiError;
			}
				
			//月チェック
			Long count = IntStream.rangeClosed(1, 12).filter(m -> m == month).count();
			if(count == 0) {
				apiError.setMessage(ErrorMessages.MONTHDATEBATMESSAGE);
				return apiError;
			}
			//日付チェック(有効な数字か)
			Long value = IntStream.rangeClosed(1, 31).filter(d -> d == day).count();
			if(value == 0) {
				apiError.setMessage(ErrorMessages.DAYDATEBATMESSAGE);
				return apiError;
			}
			
			incomeService.incomeRegister(income);
			return income;
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
		
	}
}
