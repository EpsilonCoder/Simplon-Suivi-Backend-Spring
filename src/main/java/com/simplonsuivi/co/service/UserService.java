package com.simplonsuivi.co.service;

import java.util.List;

import javax.mail.MessagingException;

import com.simplonsuivi.co.domain.User;
import com.simplonsuivi.co.exception.domain.EmailExistException;
import com.simplonsuivi.co.exception.domain.UserNotFoundException;
import com.simplonsuivi.co.exception.domain.UsernameExistException;

public interface UserService {

	
	User register(String firstName,String lastName,String username,String email)throws UserNotFoundException, UsernameExistException, EmailExistException;
	
	List<User> getUser();
	
	User findUserByUsername(String username);
	
	User findUserByEmail(String Email);
	
}
