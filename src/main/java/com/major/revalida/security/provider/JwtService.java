package com.major.revalida.security.provider;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.major.revalida.login.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET_KEY = "3777217A25432A462D4A614E645267556A586E3272357538782F413F4428472B4B6250655368566D5970337336763979244226452948404D635166546A576E5A";

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(User user) {
		if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("role", user.getAppUserRole());
		claims.put("email", user.getEmail());
		claims.put("sub", user.getUserNo()); // use user ID as the subject
		claims.put("iat", new Date().getTime() / 1000); // set the issued at time in seconds
		claims.put("exp", new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)); // set the expiration time in seconds (1 hour from now)
		return Jwts.builder()
				.setClaims(claims)
				.signWith(getSigningKey(), SignatureAlgorithm.HS512)
				.compact();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            // handle exception here (e.g. log it or return null)
            throw new IllegalArgumentException("Invalid token", e);
        }
	}
	
	public String extractUsername(String token) {
		 try {
	            return extractClaim(token, Claims::getSubject);
	        } catch (JwtException e) {
	            // handle exception here (e.g. log it or return null)
	            throw new IllegalArgumentException("Invalid token", e);
	        }
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
