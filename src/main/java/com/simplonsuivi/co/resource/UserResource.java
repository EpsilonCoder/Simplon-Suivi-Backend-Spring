package com.simplonsuivi.co.resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplonsuivi.co.constant.SecurityConstant;
import com.simplonsuivi.co.domain.User;
import com.simplonsuivi.co.domain.UserPrincipal;
import com.simplonsuivi.co.exception.domain.EmailExistException;
import com.simplonsuivi.co.exception.domain.ExceptionHandling;
import com.simplonsuivi.co.exception.domain.UserNotFoundException;
import com.simplonsuivi.co.service.UserService;
import com.simplonsuivi.co.utility.JWTTokenProvider;

@RestController
@RequestMapping(value = "/user")
public class UserResource extends ExceptionHandling {
	
	private UserService userService;
	
	private AuthenticationManager authenticationManager;
	
	private JWTTokenProvider jwtTokenProvider;
	
	@Autowired
	public UserResource(UserService userService, AuthenticationManager authenticationManager,
			JWTTokenProvider jwtTokenProvider) {
		super();
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }
	
	private HttpHeaders getJwtHeader(UserPrincipal user) {
		HttpHeaders headers=new HttpHeaders();
		headers.add(SecurityConstant.JWT_TOKEN_HEARDER, jwtTokenProvider.generateJwtToken(user));
		return headers;
	}
	private void authenticate(String username, String password) {
	  
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		
		
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, com.simplonsuivi.co.exception.domain.UsernameExistException, EmailExistException{		
	 User newUser=	userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
	 return new ResponseEntity<>(newUser,HttpStatus.OK);
	}

}
