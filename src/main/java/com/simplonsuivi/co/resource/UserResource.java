package com.simplonsuivi.co.resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplonsuivi.co.exception.domain.EmailNotFoundException;
import com.simplonsuivi.co.exception.domain.ExceptionHandling;

@RestController
@RequestMapping(value = "/user")
public class UserResource extends ExceptionHandling {
	
	@GetMapping("/home")
	public String showUser() throws  EmailNotFoundException {
		//return "Notre application est entrain de fonctionner";
		
		throw new  EmailNotFoundException("Cette addresse email  est incorrecte ");
	}

}
