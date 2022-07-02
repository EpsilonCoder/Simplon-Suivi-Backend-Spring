package com.simplonsuivi.co.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.simplonsuivi.co.domain.User;

@CrossOrigin("http://localhost:4200")
public interface UserRepository extends JpaRepository<User, Long> {

	
	User findUserByUsername(String username);

    User findUserByEmail(String email);
}
