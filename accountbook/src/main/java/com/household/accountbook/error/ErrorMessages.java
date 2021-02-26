package com.household.accountbook.error;

public class ErrorMessages {

	//202
	public static final String ERRORMESSAGE_202 = "202 Accepted リクエストは受理されましたが、内部でエラーが発生しました。処理は完了していません。";
	
	//204
	public static final String ERRORMESSAGE_204 = "204 No Content リクエストは受理されましたが、情報を取得できませんでした。";
	
	//400
	public static final String ERRORMESSAGE_400 = "400 Bad Request 構文が無効です";
	
	//401
	public static final String ERRORMESSAGE_401 = "401 Unauthorized 未認証です";
	
	//403
	public static final String ERRORMESSAGE_403 = "403 Forbidden 認証情報がありません。アクセスに失敗しました。";

	//404
	public static final String ERRORMESSAGE_404 = "404 Not found リクエストされたURLは存在しません。";
	
	//408
	public static final String ERRORMESSAGE_408 = "408 Request Timeout タイムアウトしました";
	
	//413
	public static final String ERRORMESSAGE_413 = "413 Payload Too Large 送信されたデータの上限を超えています";
	
	//414
	public static final String ERRORMESSAGE_414 = "414 URI Too Long 扱えないURLです";

	//500
	public static final String ERRORMESSAGE_500 = "500 Internal Server Error　サーバ内部でエラーが発生しました。";
	
	//データ不備
	public static final String DATEEMPTYMESSAGE = "データに不備があり情報を取得できませんでした";
	
	//（月）不備
	public static final String MONTHDATEBATMESSAGE = "日付(月)のデータが無効です。";
	
	//（日）不備
	public static final String DAYDATEBATMESSAGE = "日付(日)のデータが無効です。";
	
	//カテゴリ残１
	public static final String NOTCATEGORYDELETE = "カテゴリは最低でも1つ残してください";

	//カテゴリ最大数
	public static final String MAXCATEGORYREGISTER = "登録できるカテゴリは30個までです";

	

	

	

	
	
}
