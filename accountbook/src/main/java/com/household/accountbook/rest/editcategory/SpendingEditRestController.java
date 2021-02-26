package com.household.accountbook.rest.editcategory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@RequestMapping("/spendingcategorylist/{accountId}")
	public Object spendinggetList(@PathVariable int accountId, ModelAndView mav) {
		
		//認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		try {
			Account account = accountService.loginExamination(loginId);
			//URLのIdと認証済のID照合
			if(accountId != account.getId()) {
				apiError.setErrorCode(403);
				apiError.setMessage(ErrorMessages.ERRORMESSAGE_403);
				return apiError;
			} else {
				List<SpendingCategory> spendingCategoryList = spendingCategoryService.GetRegisteredSpendingCategory(accountId);
				return spendingCategoryList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			apiError.setMessage(ErrorMessages.ERRORMESSAGE_500);
			return apiError;
		}
		
	}
	
	//新規追加
	@RequestMapping("/addnewrequest")
	public Object addNewSpendingCategory(@RequestBody SpendingCategory spendingCategory) {
		System.out.println("addNewCategory");
		try {
			if(spendingCategory.getSpendingCategoryName().equals("") || 
			   spendingCategory.getSpendingCategoryColor().equals("")) 
			{
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}
			//登録済カテゴリ取得
			List<SpendingCategory> registedList = spendingCategoryService.GetRegisteredSpendingCategory(spendingCategory.getAccountId());
			System.out.println("==" + registedList);
			if(registedList.size() >= 30) {
				//登録カテゴリが30以上の場合
				apiError.setMessage(ErrorMessages.MAXCATEGORYREGISTER);
				return apiError;
			}
			String inputSpendingCategoryName = spendingCategory.getSpendingCategoryName();
			Stream<SpendingCategory> check = registedList
					.parallelStream()
					.filter(s -> s.getSpendingCategoryName().equals(inputSpendingCategoryName));
			if(check.count() > 0) {
				//同じカテゴリ名があった場合
				apiError.setMessage(inputSpendingCategoryName + "は登録済です");
				return apiError;
			} else {
				List<SpendingCategory> indertSpendingCategory = spendingCategoryService.addNewSpendingCategory(spendingCategory);
				return indertSpendingCategory;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}
	
	//カテゴリ名編集
	@RequestMapping("/changecategory")
	public Object changeSpendingCategory(@RequestBody ChengeCategory chengeCateogory) {
		System.out.println("changeCategory");
		
		//認証情報からログインId取得
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		try {
			int accountId = chengeCateogory.getAccountId();
			Object obj = authenticationInformation.iDVerificationcheck(loginId, accountId);
			//URLのIdと認証済のID照合
			if(obj instanceof ApiError) {
				return apiError = (ApiError) obj;
			} 
			System.out.println("userRequestAccountId:" + accountId);
			System.out.println("authId: " + obj);
			List<Object> requestData = Arrays.asList(
					chengeCateogory.getBeforeName(),
					chengeCateogory.getAfterName(),
					chengeCateogory.getAfterColor());
			Long check = requestData
					.stream()
					.filter(s -> s == null || s.equals(""))
					.count();
			if(check > 0) {
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}
			
			List<SpendingCategory> changedSpendingCategory = spendingCategoryService.changeSpendingCategory(chengeCateogory);
			return changedSpendingCategory;
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}
	
	//削除
	@RequestMapping("/deletecategory")
	public Object deleteSpendingCategory(@RequestBody DeleteCategory deleteCategory) {
		System.out.println("deleteCategory");//認証情報からログインId取得
		
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		try {
			int accountId = deleteCategory.getAccountId();
			Object obj = authenticationInformation.iDVerificationcheck(loginId, accountId);
			//URLのIdと認証済のID照合
			if(obj instanceof ApiError) {
				return apiError = (ApiError) obj;
			}
			String category = deleteCategory.getDeleteCategory();
			if(category.equals("") || category == null)  {
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}
			System.out.println("userRequestAccountId:" + accountId);
			System.out.println("authId: " + obj);
			
			//削除する前に残りカテゴリ数をチェック
			int num = spendingCategoryService.checkTheRemainingNumber(accountId);
			if(num == 1) {
				apiError.setMessage(ErrorMessages.NOTCATEGORYDELETE);
				return apiError;
			}
			
			//削除
			List<SpendingCategory> deletedSpendingCategory = spendingCategoryService.deletedSpendingCategory(deleteCategory);
			return deletedSpendingCategory;
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
		
		
	}
}
