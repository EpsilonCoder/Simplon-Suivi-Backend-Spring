package com.simplonsuivi.co.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static com.simplonsuivi.co.constant.UserImplConstant.*;
import static com.simplonsuivi.co.constant.FileConstant.*;

import com.simplonsuivi.co.constant.FileConstant;
import com.simplonsuivi.co.domain.User;
import com.simplonsuivi.co.domain.UserPrincipal;
import com.simplonsuivi.co.enumeration.Role;

import static com.simplonsuivi.co.enumeration.Role.*;
import com.simplonsuivi.co.exception.domain.EmailExistException;
import com.simplonsuivi.co.exception.domain.EmailNotFoundException;
import com.simplonsuivi.co.exception.domain.UserNotFoundException;
import com.simplonsuivi.co.exception.domain.UsernameExistException;
import com.simplonsuivi.co.repository.UserRepository;
import com.simplonsuivi.co.service.EmailSercice;
import com.simplonsuivi.co.service.LoginAttemptService;
import com.simplonsuivi.co.service.UserService;

@Service
@Transactional
@Qualifier("UserDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private LoginAttemptService loginAttemptService;
	@Autowired
	private EmailSercice emailSercice;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, LoginAttemptService loginAttemptService) {
		this.userRepository = userRepository;
		this.loginAttemptService = loginAttemptService;
		this.emailSercice = emailSercice;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findUserByUsername(username);

		if (user == null) {
			LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
			throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
		} else {
			try {
				validateLoginAttempt(user);
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			user.setLastLoginDateDisplay(user.getLastLoginDate());
			user.setLastLoginDate(new Date());
			userRepository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user);
			LOGGER.info("Utilsateur trouve :" + username);

			return userPrincipal;
		}

	}

	private void validateLoginAttempt(User user) throws ExecutionException {
		if (user.isNotlocked()) {
			if (loginAttemptService.hasExceededMaxAttemps(user.getUsername())) {
				user.setNotlocked(false);
			} else {
				user.setNotlocked(true);
			}

		}

	}

	@Override
	public User register(String firstName, String lastName, String username, String email)
			throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {

		validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
		User user = new User();
		user.setUserId(generateUserId());
		String password = generatePassword();
		String encodedPassword = encodePassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setUsername(username);
		user.setJoinDate(new Date());
		user.setPassword(encodedPassword);
		user.setActive(true);
		user.setNotlocked(true);
		user.setRole(ROLE_USER.name());
		user.setAuthorities(ROLE_USER.getAuthorities());
		user.setProfileImageUrl(getTemporaryProfileImageUrl1(username));
		userRepository.save(user);
		emailSercice.sendNewPasswordEmail(firstName, password, email);
		LOGGER.info("Nouveau mot de passe" + password);
		return user;

	}

	 private String getTemporaryProfileImageUrl1(String username) {
	        return ServletUriComponentsBuilder.fromCurrentContextPath().path(com.simplonsuivi.co.constant.FileConstant.DEFAULT_USER_IMAGE_PATH.concat(username)).toUriString();
	    }


	private String encodePassword(String password) {

		return passwordEncoder.encode(password);
	}

	private String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(12);
	}

	private String generateUserId() {
		return RandomStringUtils.randomNumeric(12);
	}

	private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
			throws UserNotFoundException, UsernameExistException, EmailExistException {
		User userByNewUsername = findUserByUsername(newUsername);
		User userByNewEmail = findUserByEmail(newEmail);
		if (StringUtils.isNotBlank(currentUsername)) {
			User currentUser = findUserByUsername(currentUsername);
			if (currentUser == null) {
				throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
			}
			if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
				throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
			}
			if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
				throw new EmailExistException(EMAIL_ALREADY_EXISTS);
			}
			return currentUser;
		} else {
			if (userByNewUsername != null) {
				throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
			}
			if (userByNewEmail != null) {
				throw new EmailExistException(EMAIL_ALREADY_EXISTS);
			}
			return null;
		}
	}

	@Override
	public List<User> getUser() {
		return userRepository.findAll();
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public User findUserByEmail(String Email) {

		return userRepository.findUserByEmail(Email);
	}

	@Override
	public User addNewUser(String firstName, String lastName, String username, String email, String role,
			boolean isNonLocked, boolean isActive, MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, Exception {

		validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
		User user = new User();
		String password = generatePassword();
		String encodedPassword = encodePassword(password);
		user.setUserId(generateUserId());
		user.setFirstName(firstName);
		user.setJoinDate(new Date());
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(encodedPassword);
		user.setActive(isActive);
		user.setNotlocked(isNonLocked);
		user.setRole(getRoleEnumName(role).name());
		user.setAuthorities(this.getRoleEnumName(role).getAuthorities());
		user.setProfileImageUrl(getTemporaryProfileImageUrl1(username));
		userRepository.save(user);
		saveProfileImage(user, profileImage);
		return user;
	}

	private String getTemporaryProfileImageUrl(String username) {

		return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username)
				.toUriString();
	}

	private Role getRoleEnumName(String role) {
		return Role.valueOf(role.toUpperCase());
	}

	@Override
	public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
			String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, Exception {

		User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);

		currentUser.setFirstName(newFirstName);
		currentUser.setLastName(newLastName);
		currentUser.setUsername(newUsername);
		currentUser.setEmail(newEmail);
		currentUser.setActive(isActive);
		currentUser.setNotlocked(isNonLocked);
		currentUser.setRole(getRoleEnumName(role).name());
		currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
		userRepository.save(currentUser);
		saveProfileImage(currentUser, profileImage);
		return currentUser;

	}


	@Override
	public void resetPassword(String email) throws EmailNotFoundException, MessagingException {

		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			throw new EmailNotFoundException(NO_FOUND_USER_BY_EMAIL + email);
		}

		String password = generatePassword();
		user.setPassword(encodePassword(password));
		userRepository.save(user);
		emailSercice.sendNewPasswordEmail(user.getFirstName(), password, user.getEmail());

	}

	@Override
	public User updateProfileImage(String username, MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
		User user = validateNewUsernameAndEmail(username, null, null);
		saveProfileImage(user, profileImage);
		return user;
	}

	private void saveProfileImage(User user, MultipartFile profileImage) throws IOException {
		
		if(profileImage !=null) {
			Path userFolder=Paths.get(USER_FOLDER+user.getUsername()).toAbsolutePath().normalize();
			if(profileImage!=null) {
				Files.createDirectories(userFolder);
				LOGGER.info(DIRECTORY_CREATED +userFolder);
			}
			Files.deleteIfExists(Paths.get(userFolder+user.getUsername()+DOT+JPG_EXTENSION));
			Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername()+DOT+JPG_EXTENSION),StandardCopyOption.REPLACE_EXISTING);	
			user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
			userRepository.save(user);
			LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM+profileImage.getOriginalFilename());
		}
	}

	private String setProfileImageUrl(String username) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH+username+FORWARD_SLASH+username+DOT+JPG_EXTENSION).toUriString();
	}

	
	   @Override
	    public void deleteUser(String username) throws UserNotFoundException, IOException {
	        User user = this.userRepository.findUserByUsername(username);
	        if (user == null) {
	            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME.concat(username));
	        }
	        Path userFolder = Paths.get(FileConstant.USER_FOLDER.concat(user.getUsername())).toAbsolutePath().normalize();
	        FileUtils.deleteDirectory(new File(userFolder.toString()));
	        this.userRepository.deleteById(user.getId());
	    }

	

}
