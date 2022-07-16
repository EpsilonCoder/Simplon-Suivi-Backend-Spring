package com.simplonsuivi.co.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplonsuivi.co.domain.User;


public interface UserRepository extends JpaRepository<User, Long> {

	
	User findUserByUsername(String username);

    User findUserByEmail(String email);
    
	List<User> findBySituationsIsFalse();
	
	List<User> findBySituationsIsTrue();
    
    
}
