package com.courzelo.courzelo_core.auth.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.courzelo.courzelo_core.auth.config.AppProperties;
import com.courzelo.courzelo_core.auth.dto.LocalUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

	@Value("${courzelo.app.jwtSecret}")
	private String tokenSecret;
	
	@Value("${courzelo.app.jwtExpirationMs}")
	private long tokenExpirationMsec;
	
	@Autowired
	private AppProperties appProperties;

	public String createToken(Authentication authentication) {
		LocalUser userPrincipal = (LocalUser) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + tokenExpirationMsec);

		return Jwts.builder()
				.setSubject(Long.toString(userPrincipal.getUser().getId()))
				.setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, tokenSecret)
				.compact();
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}
}