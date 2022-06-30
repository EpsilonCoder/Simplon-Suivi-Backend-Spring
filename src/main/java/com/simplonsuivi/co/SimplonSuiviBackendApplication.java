package com.simplonsuivi.co;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.simplonsuivi.co.constant.FileConstant;

@SpringBootApplication
public class SimplonSuiviBackendApplication  {

	public static void main(String[] args) {
		SpringApplication.run(SimplonSuiviBackendApplication.class, args);
		
		new File(FileConstant.USER_FOLDER).mkdirs();
	}
	
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
