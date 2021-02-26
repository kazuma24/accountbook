package com.household.accountbook.entity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {

	 @BeforeAll
	  static void beforeAll() {
	    System.out.println("AccountTest 開始");
	  }

	  // テスト開始後に1回だけ実行される
	  @AfterAll
	  static void afterAll() {
	    System.out.println("AccountTest 終了");
	  }

	  // 各テストメソッド開始前に実行される
	  @BeforeEach
	  void beforeEach() {
	    System.out.println("====================================開始====================================");
	  }

	  // 各テストメソッド開始後に実行される
	  @AfterEach
	  void afterEach() {
	    System.out.println("====================================終了====================================");
	  }
	  
	  @Test
	  void account() {
		  System.out.println("AccountClass getterチェック");
		  Account account = new Account(1,"testUser","test@co.jp","loginId","password");
		  assertAll(
		     () -> assertEquals(1, account.getId()),
		     () -> assertEquals("testUser", account.getName()),
		     () -> assertEquals("test@co.jp", account.getEmail()),
		     () -> assertEquals("loginId", account.getLoginId()),
		     () -> assertEquals("password", account.getPassword())
		  );
	  }

}
