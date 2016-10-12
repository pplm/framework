package org.pplm.framework.spring.dudu.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BusinessResponse<T> {
	
	public static final int RESPONSE_CODE_SUCCESS = 0;
	public static final int RESPONSE_CODE_FAILURE = 1;
	
	public static final String RESPONSE_MESSAGE_SUCCESS = "success";
	public static final String RESPONSE_MESSAGE_FAILURE = "failure";
	
	private int code;
	private String message;
	private T body;
	
	private BusinessResponse() {
		super();
	}

	private BusinessResponse(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	private BusinessResponse(int code, String message, T body) {
		super();
		this.code = code;
		this.message = message;
		this.body = body;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
	
	public static <T> BusinessResponse<T> success() {
		return success(null, null);
	}
	
	public static <T> BusinessResponse<T> success(String message) {
		return success(message, null);
	}
	
	public static <T> BusinessResponse<T> success(T body) {
		return success(null, body);
	}
	
	public static <T> BusinessResponse<T> success(String message, T body) {
		if (message == null) {
			message = RESPONSE_MESSAGE_SUCCESS;
		}
		return new BusinessResponse<T>(RESPONSE_CODE_SUCCESS, message, body);
	}
	
	public static <T> BusinessResponse<T> failure() {
		return failure(null, null);
	}
	
	public static <T> BusinessResponse<T> failure(String message) {
		return failure(message, null);
	}
	
	public static <T> BusinessResponse<T> failure(String message, T body) {
		if (message == null) {
			message = RESPONSE_MESSAGE_FAILURE;
		}
		return new BusinessResponse<T>(RESPONSE_CODE_FAILURE, message, body);
	}
	
	public static <T> BusinessResponse<T> build(int code, String message, T body) {
		return new BusinessResponse<T>(code, message, body);
	}
	
}
