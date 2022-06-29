package com.simplonsuivi.co.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.simplonsuivi.co.constant.UserImplConstant.*;
import com.simplonsuivi.co.domain.User;
import com.simplonsuivi.co.domain.UserPrincipal;
import static com.simplonsuivi.co.enumeration.Role.*;
import com.simplonsuivi.co.exception.domain.EmailExistException;
import com.simplonsuivi.co.exception.domain.UserNotFoundException;
import com.simplonsuivi.co.exception.domain.UsernameExistException;
import com.simplonsuivi.co.repository.UserRepository;
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

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findUserByUsername(username);

		if (user == null) {
			LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
			throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
		} else {
			user.setLastLoginDateDisplay(user.getLastLoginDate());
			user.setLastLoginDate(new Date());
			userRepository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user);
			LOGGER.info("Utilsateur trouve :" + username);

			return userPrincipal;
		}

	}

	@Override
	public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException  {
		
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
				user.setProfileImageUrl(getTemporaryProfileImageUrl());
				userRepository.save(user);
				LOGGER.info("Nouveau mot de passe" + password);
				return user;
			
			
	}

	private String getTemporaryProfileImageUrl() {
		
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH).toUriString();
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

	 private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
	        User userByNewUsername = findUserByUsername(newUsername);
	        User userByNewEmail = findUserByEmail(newEmail);
	        if(StringUtils.isNotBlank(currentUsername)) {
	            User currentUser = findUserByUsername(currentUsername);
	            if(currentUser == null) {
	                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
	            }
	            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
	                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
	            }
	            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
	                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
	            }
	            return currentUser;
	        } else {
	            if(userByNewUsername != null) {
	                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
	            }
	            if(userByNewEmail != null) {
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

}
