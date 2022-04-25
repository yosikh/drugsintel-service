package telran.drugsintel.sign.dao;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.util.Streamable;

import telran.drugsintel.sign.model.UserAccount;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
	
	Streamable<UserAccount> findByUsername(String username);
	
	Streamable<UserAccount> findByEmail(String email);
	
	Streamable<UserAccount> findByUsernameAndUpdatedAt(String username, LocalDateTime updatedDateTime);
	
	Streamable<UserAccount> findByEmailAndUpdatedAt(String email, LocalDateTime updatedDateTime);
	
}
