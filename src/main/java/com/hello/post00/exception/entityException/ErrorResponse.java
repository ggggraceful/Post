package com.hello.post00.exception.entityException;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ErrorResponse {

	private int errorCode;
	private String errorMessage;
	private String errorDetail;

	//of는 메서드
	public static ErrorResponse of(HttpStatus httpStatus, String message, String errorDetail) {
		return new ErrorResponse(httpStatus.value(), message, errorDetail);
	}


	public ErrorResponse(int errorCode, String message, String errorDetail) {
		this.errorCode = errorCode;
		this.errorMessage = message;
		this.errorDetail = errorDetail;
	}


}