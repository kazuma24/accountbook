package com.household.accountbook.rest.main;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.ApiError;
import com.household.accountbook.entity.Income;
import com.household.accountbook.entity.Spending;
import com.household.accountbook.error.ErrorMessages;

public class CommonMainRestController {
	
	public ApiError apiError;
	
	public AuthenticationInformation authenticationInformation;
	

	public CommonMainRestController(ApiError apiError, AuthenticationInformation authenticationInformation) {
		super();
		this.apiError = apiError;
		this.authenticationInformation = authenticationInformation;

	}
	
	//日付チェックメソッド
	public ApiError checkDate(int year, int month, int day) {
		// 有効な日付データかチェック
		LocalDate date = LocalDate.now();
		int currentYear = date.getYear();

		// 現在年～前年かチェック
		if (year == currentYear || year == (currentYear - 1) || year == (currentYear + 1)) {
			System.out.println("ok" + year);
		} else {
			String lastYear = String.valueOf((currentYear - 1));
			String nextYear = String.valueOf((currentYear + 1));
			apiError.setErrorCode(400);
			apiError.setMessage("日付（年）のデータが無効です。" + lastYear + "年から" + nextYear + "年まで有効です");
			return apiError;
		}

		// 月チェック
		if (IntStream.rangeClosed(1, 12).filter(m -> m == month).count() == 0) {
			apiError.setErrorCode(400);
			apiError.setMessage(ErrorMessages.MONTHDATEBATMESSAGE);
			return apiError;
		}
		
		// 日付チェック(有効な数字か)
		if (IntStream.rangeClosed(1, 31).filter(d -> d == day).count() == 0) {
			apiError.setErrorCode(400);
			apiError.setMessage(ErrorMessages.DAYDATEBATMESSAGE);
			return apiError;
		}
		
		apiError.setErrorCode(200);
		return apiError;
	}



	public <T> Object inputRegister(T t) {
		// 認証情報からログインID
		String loginId = AuthenticationInformation.getAuthenticationInformationLoginId();
		
		//支出か収入か判定
		if(t instanceof Spending) {
			//支出の場合
			Spending spending = (Spending) t;
			// ID照合チェック
			Object obj = authenticationInformation.iDVerificationcheck(loginId, spending.getAccountId());
			if (obj instanceof ApiError) {
				return apiError = (ApiError) obj;
			}
			
			//NULLチェック
			Optional<Integer> id = Optional.ofNullable(spending.getAccountId());
			Optional<Integer> amount = Optional.ofNullable(spending.getSpendingAmount());
			Optional<String> name = Optional.ofNullable(spending.getSpendingCategoryName());
			Optional<Integer> year = Optional.ofNullable(spending.getSpendingYear());
			Optional<Integer> month = Optional.ofNullable(spending.getSpendingMonth());
			Optional<Integer> day = Optional.ofNullable(spending.getSpendingDay());
			if(!id.isPresent() || !amount.isPresent() || !name.isPresent() || !year.isPresent() 
					|| !month.isPresent() || !day.isPresent()) {
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}
			
			// 空チェック
			List<Object> requestParams = Arrays.asList(id.get(), amount.get(), name.get(), year.get(), month.get(), day.get());
			requestParams.stream().forEach(System.out::println);
			if (requestParams.stream().filter(s -> s.equals("") || s.equals(0)).count() > 0) {
				System.out.println("empty");
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}
			
			//日付チェック
			ApiError check = checkDate(year.get(), month.get(), day.get());
			if(check.getErrorCode() != 200) {
				return check;
			} else {
				return spending;
			}
			
		} else {
			//収入の場合
			Income income = (Income) t;
			// ID照合チェック
			Object obj = authenticationInformation.iDVerificationcheck(loginId, income.getAccountId());
			if (obj instanceof ApiError) {
				return apiError = (ApiError) obj;
			}
			
			//NULLチェック
			Optional<Integer> id = Optional.ofNullable(income.getAccountId());
			Optional<Integer> amount = Optional.ofNullable(income.getIncomeAmount());
			Optional<String> name = Optional.ofNullable(income.getIncomeCategoryName());
			Optional<Integer> year = Optional.ofNullable(income.getIncomeYear());
			Optional<Integer> month = Optional.ofNullable(income.getIncomeMonth());
			Optional<Integer> day = Optional.ofNullable(income.getIncomeDay());
			if(!id.isPresent() || !amount.isPresent() || !name.isPresent() || !year.isPresent() 
					|| !month.isPresent() || !day.isPresent()) {
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}
			
			// 空チェック
			List<Object> requestParams = Arrays.asList(id.get(), amount.get(), name.get(), year.get(), month.get(), day.get());
			requestParams.stream().forEach(System.out::println);
			if (requestParams.stream().filter(s -> s.equals("") || s.equals(0)).count() > 0) {
				System.out.println("empty");
				apiError.setErrorCode(400);
				apiError.setMessage(ErrorMessages.DATEEMPTYMESSAGE);
				return apiError;
			}
			
			//日付チェック
			ApiError check = checkDate(year.get(), month.get(), day.get());
			if(check.getErrorCode() != 200) {
				return check;
			} else {
				return income;
			}
		}
		
	}
}
