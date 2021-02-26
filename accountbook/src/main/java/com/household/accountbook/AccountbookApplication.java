package com.household.accountbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.household.accountbook.auth.AuthenticationInformation;
import com.household.accountbook.entity.ApiError;

@SpringBootApplication
public class AccountbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountbookApplication.class, args);
	}
	@Bean
	ApiError apiError() {
		return new ApiError();
	}
	@Bean
	AuthenticationInformation authenticationInformation() {
		return new AuthenticationInformation();
	}

}

