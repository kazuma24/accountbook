package com.household.accountbook.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiError implements Serializable {

	/**
	 * RESTAPIエラークラス
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	private int errorCode;
	
	
	@JsonProperty("documentation_url")
	private String documentationUrl;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDocumentationUrl() {
		return documentationUrl;
	}

	public void setDocumentationUrl(String documentationUrl) {
		this.documentationUrl = documentationUrl;
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
