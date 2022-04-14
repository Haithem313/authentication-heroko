package com.courzelo.courzelo_core.auth.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import com.courzelo.courzelo_core.auth.dto.LocalUser;
import com.courzelo.courzelo_core.auth.dto.SignUpRequest;
import com.courzelo.courzelo_core.auth.entity.User;
import com.courzelo.courzelo_core.auth.exception.UserAlreadyExistAuthenticationException;
import com.courzelo.courzelo_core.auth.exception.UserNotFoundException;

/**
 * @author Chinna
 * @since 26/3/18
 */
public interface UserService {

	public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

	User findUserByEmail(String email);

	Optional<User> findUserById(Long id);

	LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
	
	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException;
	
	public User getByResetPasswordToken(String token);
	
	public void updatePassword(User user, String newPassword);
	
	public User getByRegisterToken(String token);
	
	public void confirmEmail(User user);
}
