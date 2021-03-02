package com.household.accountbook.rest.editcategory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.Account;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.ChengeCategory;
import com.household.accountbook.entity.DeleteCategory;
import com.household.accountbook.entity.IncomeCategory;
import com.household.accountbook.error.ErrorMessages;
import com.household.accountbook.service.AccountService;
import com.household.accountbook.service.IncomeCategoryService;

@RestController
public class IncomeEditRestController {
	@Autowired
	ApiError apiError;

	@Autowired
	AccountService accountService;

	@Autowired
	IncomeCategoryService incomeCategoryService;

	@Autowired
	AuthenticationInformation authenticationInformation;

	@RequestMapping(value = "/incomecategorylist/{accountId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public Object incomegetList(@PathVariable int accountId, ModelAndView mav) {

		// 認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		try {
			Account account = accountService.loginExamination(loginId);
			// URLのIdと認証済のID照合
			if (accountId != account.getId()) {
				System.out.println("認証エラー");
				apiError.setErrorCode(403);
				apiError.setMessage(ErrorMessages.ERRORMESSAGE_403);
				return apiError;
			} else {
				List<IncomeCategory> incomeCategoryList = incomeCategoryService.GetRegisteredIncomeCategory(accountId);
				return incomeCategoryList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			apiError.setMessage(ErrorMessages.ERRORMESSAGE_500);
			return apiError;
		}

	}

	// 新規追加
	@RequestMapping(value = "/addnewrequestincome", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public Object addNewIncomeCategory(@RequestBody IncomeCategory incomeCategory) {
		System.out.println("addNewCategory" + incomeCategory.getIncomeCategoryName() + ":");
		try {
			Object validatedData = new CommonValiDation(apiError, authenticationInformation, null,
					incomeCategoryService).addCheck(incomeCategory);
			if (validatedData instanceof ApiError) {
				return (ApiError) validatedData;
			} else {
				// Spring Test OK
				return incomeCategoryService.addNewIncomeCategory(incomeCategory);
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}

	// カテゴリ名編集
	@RequestMapping(value = "/changecategoryincome", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public Object changeIncomeCategory(@RequestBody ChengeCategory chengeCateogory) {
		System.out.println("changeCategory");

		try {
			Object validatedData = new CommonValiDation(apiError, authenticationInformation, null,
					incomeCategoryService).editCheck(chengeCateogory);
			if (validatedData instanceof ApiError) {
				return (ApiError) validatedData;
			} else {
				// OK
				return incomeCategoryService.changeIncomeCategory(chengeCateogory);
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}

	// 削除
	@RequestMapping(value = "/deletecategoryincome", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public Object deleteIncomeCategory(@RequestBody DeleteCategory deleteCategory) {
		System.out.println("deleteCategory");

		try {
			// バリデーション
			Object validatedData = new CommonValiDation(apiError, authenticationInformation, null,
					incomeCategoryService).deleteCheck(deleteCategory);
			if (validatedData instanceof ApiError) {
				return (ApiError) validatedData;
			} else {
				// 削除する前に残りカテゴリ数をチェック
				DeleteCategory data = (DeleteCategory) validatedData;
				int num = incomeCategoryService.checkTheRemainingNumber(data.getAccountId());
				if (num == 1) {
					// 残数１の場合削除停止
					apiError.setErrorCode(400);
					apiError.setMessage(ErrorMessages.NOTCATEGORYDELETE);
					return apiError;
				}
				// spending, spending_category から削除
				return incomeCategoryService.deletedIncomeCategory(deleteCategory);
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}

	}
}
