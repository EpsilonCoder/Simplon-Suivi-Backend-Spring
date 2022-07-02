package com.simplonsuivi.co;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.simplonsuivi.co.constant.FileConstant;

import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
@EnableWebMvc
@EnableSwagger2
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
