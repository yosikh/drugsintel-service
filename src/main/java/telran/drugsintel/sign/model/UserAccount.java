package telran.drugsintel.sign.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = {"id", "username", "email", "updatedAt"})
@Document(collection = "users")
public class UserAccount {
	String id;
	@Setter
	String username;
	@Setter
	String email;
	@Setter
	String password;
	String role;
	@Setter
	LocalDateTime updatedAt;
	
	public UserAccount() {
		this.role = "guest";
		this.updatedAt = LocalDateTime.now();
	}

	public UserAccount(String username, String email, String password) {
		this();
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
}

