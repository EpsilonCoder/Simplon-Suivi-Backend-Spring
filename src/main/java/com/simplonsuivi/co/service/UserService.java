package com.simplonsuivi.co.service;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.simplonsuivi.co.domain.User;
import com.simplonsuivi.co.exception.domain.EmailExistException;
import com.simplonsuivi.co.exception.domain.EmailNotFoundException;
import com.simplonsuivi.co.exception.domain.UserNotFoundException;
import com.simplonsuivi.co.exception.domain.UsernameExistException;
public interface UserService {

	
	User register(String firstName,String lastName,String username,String email)throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException;
	
	List<User> getUser();
	
	User findUserByUsername(String username);
	
	User findUserByEmail(String Email);
	
	User addNewUser(String firstName,String lastName,String username,String email,String role,boolean isNonLocked,boolean isActive , MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, Exception;
	
	User updateUser(String currentUsername,String newFirstName,String newLastName,String newUsername,String newEmail,String role,boolean isNonLocked,boolean isActive , MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException,  Exception;
	
	void deleteUser(String username) throws IOException, UserNotFoundException;
	
	void resetPassword(String email) throws EmailNotFoundException, MessagingException;
	
	User updateProfileImage(String username,MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException;
	
	
}
