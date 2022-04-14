package com.courzelo.courzelo_core.auth.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courzelo.courzelo_core.auth.dto.ApiResponse;
import com.courzelo.courzelo_core.auth.dto.JwtAuthenticationResponse;
import com.courzelo.courzelo_core.auth.dto.LocalUser;
import com.courzelo.courzelo_core.auth.dto.LoginRequest;
import com.courzelo.courzelo_core.auth.dto.SignUpRequest;
import com.courzelo.courzelo_core.auth.entity.User;
import com.courzelo.courzelo_core.auth.exception.UserAlreadyExistAuthenticationException;
import com.courzelo.courzelo_core.auth.exception.UserNotFoundException;
import com.courzelo.courzelo_core.auth.service.UserService;
import com.courzelo.courzelo_core.auth.util.GeneralUtils;
import com.courzelo.courzelo_core.auth.security.jwt.TokenProvider;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserService userService;

	@Autowired
	TokenProvider tokenProvider;
	
	@Autowired	
	JavaMailSender mailSender;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.createToken(authentication);
		LocalUser localUser = (LocalUser) authentication.getPrincipal();
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, GeneralUtils.buildUserInfo(localUser)));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		try {
			userService.registerNewUser(signUpRequest);
		} catch (UserAlreadyExistAuthenticationException e) {
			log.error("Exception Ocurred", e);
			return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
	}
	
	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request) throws UserNotFoundException, UnsupportedEncodingException, MessagingException {
	    String email = request.getParameter("email");
	    String token = RandomString.make(30);
	     
	 //   try {
	        userService.updateResetPasswordToken(token, email);
	        String resetPasswordLink = "http://localhost:4200/reset?token=" + token;
	        sendEmail(email, resetPasswordLink);
	         
	 /*   } catch (UserNotFoundException ex) {
	        model.addAttribute("error", ex.getMessage());
	    } catch (UnsupportedEncodingException | MessagingException e) {
	        model.addAttribute("error", "Error while sending email");
	    }*/
	    System.out.println(resetPasswordLink);
	    return token;
	}
	
	public void sendEmail(String recipientEmail, String link)
	        throws MessagingException, UnsupportedEncodingException {
	    MimeMessage message = mailSender.createMimeMessage();              
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom("elmetoui.haithem@gmail.com", "Courzelo Support");
	    helper.setTo(recipientEmail);
	     
	    String subject = "Here's the link to reset your password";
	     
	    String content = "<p>Hello,</p>"
	            + "<p>You have requested to reset your password.</p>"
	            + "<p>Click the link below to change your password:</p>"
	            + "<p><a href=\"" + link + "\">Change my password</a></p>"
	            + "<br>"
	            + "<p>Ignore this email if you do remember your password, "
	            + "or you have not made the request.</p>";
	     
	    helper.setSubject(subject);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	}
	
	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request) {
	    String token = request.getParameter("token");
	    String password = request.getParameter("password");
	    
	    User user = userService.getByResetPasswordToken(token);
	   /* if (user == null) {
	        model.addAttribute("message", "Invalid Token");
	        return "message";
	    } else {   */        
	        userService.updatePassword(user, password);
	         
	     //   model.addAttribute("message", "You have successfully changed your password.");
	   // }
	     
	    return "message";
	}
	
	@PostMapping("/confirm_email")
	public String confirmEmail(HttpServletRequest request) {
	    String token = request.getParameter("token");
	    User user = userService.getByRegisterToken(token); 
	    userService.confirmEmail(user);
	    return "message";
	}
}