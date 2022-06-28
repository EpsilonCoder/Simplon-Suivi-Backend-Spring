package com.simplonsuivi.co.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.simplonsuivi.co.domain.User;
import com.simplonsuivi.co.domain.UserPrincipal;
import com.simplonsuivi.co.repository.UserRepository;
import com.simplonsuivi.co.service.UserService;

@Service
@Transactional
@Qualifier("UserDetailsService")
public class UserServiceImpl implements UserService,UserDetailsService {

	private Logger LOGGER=LoggerFactory.getLogger(getClass());
	@Autowired	
	private  UserRepository userRepository;
	
	public UserServiceImpl( UserRepository userRepository) {
		this.userRepository=userRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=userRepository.findUserByUsername(username);
		
		if(user==null) {
			LOGGER.error("utilisateur n 'existe pas "+username );
			throw new UsernameNotFoundException("L'utilisateur n 'existe pas "+username );
		}else {
			user.setLastLoginDateDisplay(user.getLastLoginDate());
			user.setLastLoginDate(new Date());
			userRepository.save(user);
			UserPrincipal userPrincipal=new UserPrincipal(user);
			LOGGER.info("Utilsateur trouve :"+username);
			
			return userPrincipal;
		}

	}

}
