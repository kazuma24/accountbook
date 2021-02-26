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
import com.household.accountbook.entity.Spending;
import com.household.accountbook.entity.SpendingCategory;
import com.household.accountbook.error.ErrorMessages;
import com.household.accountbook.service.AccountService;
import com.household.accountbook.service.SpendingCategoryService;
import com.household.accountbook.service.SpendingService;

@RestController
public class SpendingMainRestController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	SpendingCategoryService spendingCategoryService;
	
	@Autowired
	SpendingService spendingService;
	
	@Autowired
	ApiError apiError;
	
	@Autowired
	AuthenticationInformation authenticationInformation;
	

	Logger logger = Logger.getLogger(SpendingMainRestController.class.getName());
			
	@RequestMapping("/categorycheck")
	public Object CategoryCheck() {
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		logger.info("ログインID" + loginId);
		if(loginId.equals("anonymousUser")) {
			//ログイン認証情報がanonymousUserの場合
			logger.info("認証情報なし" + loginId);
			apiError.setErrorCode(403);
			return apiError;
		}
		try {
			List<SpendingCategory> spendingCategory =  spendingCategoryResourceAcquisition(loginId);
			return spendingCategory;
		} catch (SQLException e) {
			//SQLエラーの場合
			logger.info("spendingCategoryResourceAcquisition():SQLException" + e.getCause());
			apiError.setErrorCode(202);
			return apiError;
		} catch (Exception e) {
			//何らかのエラーの場合
			logger.info("spendingCategoryResourceAcquisition():Exception" + e.getCause());
			apiError.setErrorCode(204);
			return apiError;
		}

    }
	
	@Transactional
	public List<SpendingCategory> spendingCategoryResourceAcquisition(String loginId) throws Exception, SQLException {
		try {
			Optional<Object> OptionalId = Optional.ofNullable(accountService.spendingCategoryCheckAndIdAcquisition(loginId));
			if(OptionalId.isPresent()) {
				//デフォルトアカウント（初期ユーザー）
				int id = (int) OptionalId.get();
				logger.info("spending_categoryに登録なし。デフォルトの支出カテゴリを設定し、設定したカテゴリとカラーを取得する");
				List<SpendingCategory> spendingCategory = spendingCategoryService.DefaultCategorySettingAndGet(id);
				return spendingCategory;
			} else {
				//支出カテゴリテーブルデータありの場合
				logger.info("spending_categoryに登録あり。登録済の支出カテゴリを取得しする");
				List<SpendingCategory> spendingCategory = spendingCategoryService.SetSpendingCategory(loginId);
				return spendingCategory;
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@RequestMapping("/spendingRegisterRequest")
	public Object spendingRegister(@RequestBody Spending spending) {
		logger.info("spendingRegister(): body " + spending);
		System.out.println("spendingRegister(): body " + spending);
		try {
			//認証情報からログインID
			String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
			//ID照合チェック
			Object obj = authenticationInformation.iDVerificationcheck(loginId, spending.getAccountId());
			if(obj instanceof ApiError) {
				return apiError = (ApiError) obj;
			}
			//null 空文字　チェック
			List<Object> requestParams = Arrays.asList(
					spending.getAccountId(), spending.getSpendingAmount(),
					spending.getSpendingCategoryName(), spending.getSpendingYear(),
					spending.getSpendingMonth(), spending.getSpendingDay());
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
			

			int year = spending.getSpendingYear();
			int month = spending.getSpendingMonth();
			int day = spending.getSpendingDay();
			
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
			
			spendingService.spendingRegister(spending);
			return spending;
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
		
	}
}
