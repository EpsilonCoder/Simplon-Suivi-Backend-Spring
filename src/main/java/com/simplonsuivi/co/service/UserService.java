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

	
	User register(String firstName,String lastName,String username,String email,String telepone)throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException;
	
	List<User> getUser();
	
	List<User> getUserAlafabrique();
	
	List<User> getUserEnEntreprise();
	
	User findUserByUsername(String username);
	
	User findUserByEmail(String Email);
	
	User findUserByTelephone(String Telephone);
	
	User addNewUser(String firstName,String lastName,String username,String email,String telephone,String role,boolean isNonLocked,boolean isActive ,boolean situations ,boolean entretien, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, Exception;
	
	User updateUser(String currentUsername,String newFirstName,String newLastName,String newUsername,String newEmail,String newTelephone,String role,boolean isNonLocked,boolean isActive , boolean situations ,  boolean entretien ,MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException,  Exception;
	
	void deleteUser(String username) throws IOException, UserNotFoundException;
	
	void resetPassword(String email) throws EmailNotFoundException, MessagingException;
	
	User updateProfileImage(String username,MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException;
	
	
}
