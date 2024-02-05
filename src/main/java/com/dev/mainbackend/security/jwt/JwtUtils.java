package com.dev.mainbackend.security.jwt;

import io.jsonwebtoken.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${bsu.app.jwtAccessExpirationMs}")
	private int jwtAccessExpirationMs;
	@Value("${bsu.app.jwtRefreshExpirationMs}")
	private int jwtRefreshExpirationMs;
	@Getter
	@Value("${bsu.app.jwtSecret}")
	private String jwtSecret;

	public  boolean isRefreshTokenValid(String refreshToken) {
		try {
			Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(refreshToken).getBody();

			Date expirationDate = claims.getExpiration();
			if (expirationDate.before(new Date())) {
				return false;
			}
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.info("JWT token is expired: {}", e.getLocalizedMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}




		return true;
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getLocalizedMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public String generateJwtAccessToken(String userName) {

//		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder().setSubject(userName).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtAccessExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	public String generateJwtRefreshToken(String userName) {

//		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder().setSubject(userName)
				.setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

	}


	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

}
