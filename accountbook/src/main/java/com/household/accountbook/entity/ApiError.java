package com.household.accountbook.entity;

import java.io.Serializable;

public class ApiError implements Serializable {

	/**
	 * RESTAPIエラークラス
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	private int errorCode;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
