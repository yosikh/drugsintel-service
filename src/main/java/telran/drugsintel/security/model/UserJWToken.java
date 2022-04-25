package telran.drugsintel.security.model;

import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

@Getter
public class UserJWToken {
	private static final String signature = "aHR0cHM6Ly93d3cuZHJ1Z3NpbnRlbC5jb20="; //https://www.drugsintel.com
	private static final long TIME_EXPIRATION = 30 * 60 * 1000;
	
	String login;
	String userId;
	String token;

	public UserJWToken(String username, String userId) {
		this.login = username;
		this.userId = userId;
	}
	
	public UserJWToken(String token) {
		this.token = token;
	}
		
	public void doGenerateToken() {
		try {
			token = Jwts.builder()
							.claim("login", login)
							.claim("userId", userId)
							.setExpiration(new Date(System.currentTimeMillis() + TIME_EXPIRATION))
							.signWith(SignatureAlgorithm.HS512, Base64.getDecoder().decode(signature))
							.compact();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	public boolean validateToken() {
		Claims claims;
		try {
			claims = Jwts.parser()
							.setSigningKey(Base64.getDecoder().decode(signature))
							.parseClaimsJws(token)
							.getBody();
			userId = claims.get("userId").toString();
		} catch(Exception e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
