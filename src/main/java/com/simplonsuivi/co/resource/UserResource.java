package com.simplonsuivi.co.resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplonsuivi.co.domain.User;
import com.simplonsuivi.co.exception.domain.EmailExistException;
import com.simplonsuivi.co.exception.domain.ExceptionHandling;
import com.simplonsuivi.co.exception.domain.UserNotFoundException;
import com.simplonsuivi.co.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserResource extends ExceptionHandling {
	@Autowired
	private UserService userService;
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, com.simplonsuivi.co.exception.domain.UsernameExistException, EmailExistException{		
		//return "Notre application est entrain de fonctionner";
	 User newUser=	userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
	 return new ResponseEntity<>(newUser,HttpStatus.OK);
	}

}
