package com.simplonsuivi.co.domain;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {
	
	
	private int httpStatusCode;
	@JsonFormat(shape =JsonFormat.Shape.STRING,pattern = "MM-dd-yyyy hh:mm:ss")
	private Date timeStamp;
	private HttpStatus httpStatus;
	private String reason;
	private String message;
	
	



}




