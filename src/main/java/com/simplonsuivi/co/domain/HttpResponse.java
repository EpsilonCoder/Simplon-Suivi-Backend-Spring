package com.simplonsuivi.co.domain;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HttpResponse {
	

	public HttpResponse(int value, HttpStatus forbidden, String upperCase, String forbiddenMessage) {
		
	}
	
	private int httpStatusCode;
	@JsonFormat(shape =JsonFormat.Shape.STRING,pattern = "MM-dd-yyyy hh:mm:ss")
	private Date timeStamp;
	private HttpStatus httpStatus;
	private String reason;
	private String message;
	



}




