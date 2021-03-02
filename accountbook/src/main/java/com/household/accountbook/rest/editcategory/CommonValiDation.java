package com.household.accountbook.rest.editcategory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.ChengeCategory;
import com.household.accountbook.entity.DeleteCategory;
import com.household.accountbook.entity.IncomeCategory;
import com.household.accountbook.entity.SpendingCategory;
import com.household.accountbook.error.ErrorMessages;
import com.household.accountbook.service.IncomeCategoryService;
import com.household.accountbook.service.SpendingCategoryService;

public class CommonValiDation {
	
	private ApiError apiError;

	private AuthenticationInformation authenticationInformation;
	
	public SpendingCategoryService spendingCategoryService;
	
	public IncomeCategoryService incomeCategoryService;
	
	public CommonValiDation(ApiError apiError, AuthenticationInformation authenticationInformation,
			SpendingCategoryService spendingCategoryService, IncomeCategoryService incomeCategoryService) {
		super();
		this.apiError = apiError;
		this.authenticationInformation = authenticationInformation;
		this.spendingCategoryService = spendingCategoryService;
		this.incomeCategoryService = incomeCategoryService;
	}

	//新規追加機能バリデーション
	public <T> Object addCheck(T t) {
		try {
			if(t instanceof SpendingCategory) {
				//支出カテゴリの場合
				//総称型をキャスト
				SpendingCategory spendingCategory = (SpendingCategory) t;
				
				//nullチェック
				Optional<Integer> id = Optional.ofNullable(spendingCategory.getAccountId());
				Optional<String> name = Optional.ofNullable(spendingCategory.getSpendingCategoryName());
				Optional<String> color = Optional.ofNullable(spendingCategory.getSpendingCategoryColor());
				if (!name.isPresent() || !color.isPresent()) {
					// Spring Test OK
					apiError.setErrorCode(400);
					apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
					return apiError;
				}
				// 空チェック
				List<Object> requestData = Arrays.asList(id.get(), name.get(), color.get());
				Long emptyCount = requestData.stream().filter(s -> s.equals("")).count();
				if (emptyCount > 0) {
					// Spring Test OK
					System.out.println("empty");
					apiError.setErrorCode(400);
					apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
					return apiError;
				}
				
				// 登録済カテゴリ取得
				List<SpendingCategory> registedList = spendingCategoryService
						.GetRegisteredSpendingCategory(spendingCategory.getAccountId());
				System.out.println("==" + registedList);
				if (registedList.size() >= 30) {
					// 登録カテゴリが30以上の場合
					// Spring Test OK
					apiError.setErrorCode(400);
					apiError.setMessage(ErrorMessages.MAXCATEGORYREGISTER);
					return apiError;
				}

				// 重複チェック
				Stream<SpendingCategory> check = registedList.parallelStream()
						.filter(s -> s.getSpendingCategoryName().equals(spendingCategory.getSpendingCategoryName()));
				if (check.count() > 0) {
					// 同じカテゴリ名があった場合
					// Spring Test OK
					apiError.setErrorCode(400);
					apiError.setMessage(spendingCategory.getSpendingCategoryName() + "は登録済です");
					return apiError;
				}
				
				
				return spendingCategory;
			} else {
				//収入カテゴリの場合
				
				//総称型をキャスト
				IncomeCategory incomeCategory = (IncomeCategory) t;
				
				//nullチェック
				Optional<Integer> id = Optional.ofNullable(incomeCategory.getAccountId());
				Optional<String> name = Optional.ofNullable(incomeCategory.getIncomeCategoryName());
				Optional<String> color = Optional.ofNullable(incomeCategory.getIncomeCategoryColor());
				if (!name.isPresent() || !color.isPresent()) {
					apiError.setErrorCode(400);
					apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
					return apiError;
				}
				
				// 空チェック
				List<Object> requestData = Arrays.asList(id.get(), name.get(), color.get());
				Long emptyCount = requestData.stream().filter(s -> s.equals("")).count();
				if (emptyCount > 0) {
					// Spring Test OK
					System.out.println("empty");
					apiError.setErrorCode(400);
					apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
					return apiError;
				}
				
				// 登録済カテゴリ取得
				List<IncomeCategory> registedList = incomeCategoryService
						.GetRegisteredIncomeCategory(incomeCategory.getAccountId());
				if (registedList.size() >= 30) {
					// 登録カテゴリが30以上の場合
					apiError.setErrorCode(400);
					apiError.setMessage(ErrorMessages.MAXCATEGORYREGISTER);
					return apiError;
				}

				// 重複チェック
				Stream<IncomeCategory> check = registedList.parallelStream()
						.filter(s -> s.getIncomeCategoryName().equals(incomeCategory.getIncomeCategoryName()));
				if (check.count() > 0) {
					// 同じカテゴリ名があった場合
					apiError.setMessage(incomeCategory.getIncomeCategoryName() + "は登録済です");
					return apiError;
				}
				
				return incomeCategory;
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
		
	}
	
	//カテゴリ編集機能バリデーション
	public Object editCheck(ChengeCategory chengeCateogory) {
		try {
			// 認証情報からログインId取得
			String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();

			// 必須データチェック
			Optional<Integer> id = Optional.ofNullable(chengeCateogory.getAccountId());
			Optional<String> beforeName = Optional.ofNullable(chengeCateogory.getBeforeName());
			Optional<String> afterName = Optional.ofNullable(chengeCateogory.getAfterName());
			Optional<String> afterColor = Optional.ofNullable(chengeCateogory.getAfterColor());
			if (!id.isPresent() || !beforeName.isPresent() || !afterName.isPresent() || !afterColor.isPresent()) {
				// Spring Test OK
				System.out.println("null");
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}

			// URLのIdと認証済のID照合
			int accountId = id.get();
			Object obj = authenticationInformation.iDVerificationcheck(loginId, accountId);
			if (obj instanceof ApiError) {
				// Spring Test OK
				return apiError = (ApiError) obj;
			}

			// 空チェック
			List<Object> requestData = Arrays.asList(id.get(), beforeName.get(), afterName.get(), afterColor.get());
			if (requestData.stream().filter(s -> s.equals("")).count() > 0) {
				// Spring Test OK
				System.out.println("empty");
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}

			// OK
			return chengeCateogory;

		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}

	// カテゴリ削除機能バリデーション
	public Object deleteCheck(DeleteCategory deleteCategory) {

		try {
			// 認証情報からログインId取得
			String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();

			// nullチェック
			Optional<Integer> opId = Optional.ofNullable(deleteCategory.getAccountId());
			Optional<String> opDeleteCategory = Optional.ofNullable(deleteCategory.getDeleteCategory());
			if (!opId.isPresent() || !opDeleteCategory.isPresent()) {
				System.out.println("null");
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}

			// 空チェック
			String category = opDeleteCategory.get();
			if (category.equals("")) {
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}

			// Id比較
			int accountId = opId.get();
			Object obj = authenticationInformation.iDVerificationcheck(loginId, accountId);
			// URLのIdと認証済のID照合
			if (obj instanceof ApiError) {
				return apiError = (ApiError) obj;
			}

			return deleteCategory;

		} catch (Exception e) {
			e.printStackTrace();
			apiError.setErrorCode(500);
			return apiError;
		}
	}
}
