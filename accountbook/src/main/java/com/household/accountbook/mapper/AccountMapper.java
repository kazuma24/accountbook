package com.household.accountbook.mapper;



import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.household.accountbook.entity.Account;

@Mapper
public interface AccountMapper {
	
	//アカウント登録
    int accountRegister(Account account);
    
    //アカウント情報取得
    @Select("SELECT * FROM accounts WHERE id = #{id}")
    Account fetchAccount(int id);
    
    //ログインID重複審査
    @Select("SELECT count(id) FROM accounts WHERE login_id = #{loginId}")
    int loginIdDuplicateExamination(String loginId);

    //ログイン取得
    @Select("SELECT * FROM accounts WHERE login_id = #{loginId}")
    public Account loginExamination(String loginId);
    
    //そのアカウントの支出カテゴリテーブルチェック&ID取得(spending_categoryにloginIdから取得するidがなければloginIdからidを取得する。)
	@Select("SELECT id FROM accounts WHERE login_id = #{loginId} "
			+ "AND "
			+ "NOT EXISTS (SELECT account_id FROM spending_category WHERE account_id = (SELECT id FROM accounts WHERE login_id = #{loginId}))")
	public Object spendingCategoryCheckAndIdAcquisition(String loginId);
	
	 //そのアカウントの支出カテゴリテーブルチェック&ID取得(income_categoryにloginIdから取得するidがなければloginIdからidを取得する。)
	@Select("SELECT id FROM accounts WHERE login_id = #{loginId} "
			+ "AND "
			+ "NOT EXISTS (SELECT account_id FROM income_category WHERE account_id = (SELECT id FROM accounts WHERE login_id = #{loginId}))")
	public Object incomeCategoryCheckAndIdAcquisition(String loginId);
	
	@Select("SELECT id FROM accounts WHERE id = #{accountId} AND login_id = #{loginId}")
	public Object idCollationCheck(Integer accountId, String loginId);
	
	//指定のIDのデータを全テーブルから削除
	@Delete("DELETE FROM accounts WHERE id = #{accountId};")
	public void allDelete(Integer accountId);
	
}
