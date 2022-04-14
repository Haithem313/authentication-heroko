package com.courzelo.courzelo_core.auth.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.courzelo.courzelo_core.auth.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

	User findByEmail(String email);

	boolean existsByEmail(String email);

	User findByResetPasswordToken(String token);
	
	User findByRegisterToken(String token);
}
