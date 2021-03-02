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
import com.household.accountbook.entity.Income;
import com.household.accountbook.entity.IncomeCategory;
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

	@RequestMapping(value = "/incomecategorycheck", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public Object CategoryCheck() {

		try {
			// 認証情報からログインId取得
			String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
			List<IncomeCategory> incomeCategory = incomeCategoryResourceAcquisition(loginId);
			return incomeCategory;
		} catch (Exception e) {
			// 何らかのエラーの場合
			logger.info("incomeCategoryResourceAcquisition():Exception" + e.getCause());
			apiError.setErrorCode(204);
			return apiError;
		}

	}

	@Transactional
	public List<IncomeCategory> incomeCategoryResourceAcquisition(String loginId) throws Exception, SQLException {
		try {
			Optional<Object> OptionalId = Optional
					.ofNullable(accountService.incomeCategoryCheckAndIdAcquisition(loginId));
			if (OptionalId.isPresent()) {
				// デフォルトアカウント（初期ユーザー）
				int id = (int) OptionalId.get();
				System.out.println("id " + id);
				logger.info("income_categoryに登録なし。デフォルトの収入カテゴリを設定し、設定したカテゴリとカラーを取得する");
				List<IncomeCategory> incomeCategory = incomeCategoryService.DefaultCategorySettingAndGet(id);
				return incomeCategory;
			} else {
				// 収入カテゴリテーブルデータありの場合
				logger.info("income_categoryに登録あり。登録済の収入カテゴリを取得しする");
				List<IncomeCategory> incomeCategory = incomeCategoryService.SetIncomeCategory(loginId);
				return incomeCategory;
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@RequestMapping(value = "/incomeRegisterRequest", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Object incomeRegister(@RequestBody Income income) {
		logger.info("incomeRegister(): body " + income);
		try {
			// バリデーション
			Object validatedData = new CommonMainRestController(apiError, authenticationInformation)
					.inputRegister(income);
			if (validatedData instanceof ApiError) {
				return (ApiError) validatedData;
			} else {
				// OK
				incomeService.incomeRegister(income);
				return income;
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}

	}
}
