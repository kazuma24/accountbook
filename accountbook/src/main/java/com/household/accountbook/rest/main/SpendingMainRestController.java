package com.household.accountbook.rest.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.Spending;
import com.household.accountbook.entity.SpendingCategory;
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

	@RequestMapping(value="/categorycheck", method=RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public Object CategoryCheck() {
		
		try {
			//認証情報からログインId取得
			String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
			List<SpendingCategory> spendingCategory = spendingCategoryResourceAcquisition(loginId);
			return spendingCategory;
		} catch (Exception e) {
			// 何らかのエラーの場合
			logger.info("spendingCategoryResourceAcquisition():Exception" + e.getCause());
			apiError.setErrorCode(500);
			return apiError;
		}

	}

	@Transactional
	public List<SpendingCategory> spendingCategoryResourceAcquisition(String loginId) throws Exception, SQLException {
		try {
			Optional<Object> OptionalId = Optional
					.ofNullable(accountService.spendingCategoryCheckAndIdAcquisition(loginId));
			if (OptionalId.isPresent()) {
				// デフォルトアカウント（初期ユーザー）
				int id = (int) OptionalId.get();
				logger.info("spending_categoryに登録なし。デフォルトの支出カテゴリを設定し、設定したカテゴリとカラーを取得する");
				return spendingCategoryService.DefaultCategorySettingAndGet(id);
			} else {
				// 支出カテゴリテーブルデータありの場合
				logger.info("spending_categoryに登録あり。登録済の支出カテゴリを取得する");
				return spendingCategoryService.SetSpendingCategory(loginId);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@RequestMapping(value = "/spendingRegisterRequest", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Object spendingRegister(@RequestBody Spending spending) {
		logger.info("spendingRegister(): body " + spending);
		try {
			//バリデーション
			Object validatedData = new CommonMainRestController(apiError, authenticationInformation)
					.inputRegister(spending);
			if(validatedData instanceof ApiError) {
				return (ApiError) validatedData;
			} else {
				spendingService.spendingRegister(spending);
				return spending;
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}

	}
}
