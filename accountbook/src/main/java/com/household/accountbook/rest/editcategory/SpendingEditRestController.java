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
import com.household.accountbook.entity.SpendingCategory;
import com.household.accountbook.error.ErrorMessages;
import com.household.accountbook.service.AccountService;
import com.household.accountbook.service.SpendingCategoryService;

@RestController
public class SpendingEditRestController {

	@Autowired
	ApiError apiError;

	@Autowired
	AccountService accountService;

	@Autowired
	SpendingCategoryService spendingCategoryService;

	@Autowired
	AuthenticationInformation authenticationInformation;

	@RequestMapping(value = "/spendingcategorylist/{accountId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public Object spendinggetList(@PathVariable int accountId, ModelAndView mav) {

		// 認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		try {
			Account account = accountService.loginExamination(loginId);
			// URLのIdと認証済のID照合
			if (accountId != account.getId()) {
				// Spring Test OK
				apiError.setErrorCode(403);
				apiError.setMessage(ErrorMessages.ERRORMESSAGE_403);
				return apiError;
			} else {
				// Spring Test OK
				List<SpendingCategory> spendingCategoryList = spendingCategoryService
						.GetRegisteredSpendingCategory(accountId);
				return spendingCategoryList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			apiError.setMessage(ErrorMessages.ERRORMESSAGE_500);
			return apiError;
		}

	}

	// 新規追加
	@RequestMapping(value = "/addnewrequest", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public Object addNewSpendingCategory(@RequestBody SpendingCategory spendingCategory) {
		System.out.println("addNewCategory" + spendingCategory.getSpendingCategoryName() + ":");
		try {
			Object validatedData = new CommonValiDation(apiError, authenticationInformation, spendingCategoryService, null).addCheck(spendingCategory);
			if(validatedData instanceof ApiError) {
				return (ApiError) validatedData;
			} else {
				// Spring Test OK
				return spendingCategoryService.addNewSpendingCategory(spendingCategory);
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}

	// カテゴリ名編集
	@RequestMapping(value = "/changecategory", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public Object changeSpendingCategory(@RequestBody ChengeCategory chengeCateogory) {
		System.out.println("changeCategory");

		try {
			Object validatedData = new CommonValiDation(apiError, authenticationInformation, spendingCategoryService, null).editCheck(chengeCateogory);
			
			if(validatedData instanceof ApiError) {
				return (ApiError) validatedData;
			}else {
				// OK
				return spendingCategoryService.changeSpendingCategory(chengeCateogory);
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}

	// 削除
	@RequestMapping(value = "/deletecategory", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public Object deleteSpendingCategory(@RequestBody DeleteCategory deleteCategory) {
		System.out.println("deleteCategory");

		try {
			//バリデーション
			Object validatedData = new CommonValiDation(apiError, authenticationInformation, spendingCategoryService, null).deleteCheck(deleteCategory);
			if(validatedData instanceof ApiError) {
				return (ApiError) validatedData;
			} else {
				// 削除する前に残りカテゴリ数をチェック
				DeleteCategory data = (DeleteCategory) validatedData;
				int num = spendingCategoryService.checkTheRemainingNumber(data.getAccountId());
				if (num == 1) {
					// 残数１の場合削除停止
					apiError.setErrorCode(400);
					apiError.setMessage(ErrorMessages.NOTCATEGORYDELETE);
					return apiError;
				}
				// spending, spending_category から削除
				return spendingCategoryService.deletedSpendingCategory(deleteCategory);
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}

	}
}
