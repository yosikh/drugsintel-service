package telran.drugsintel.sign.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import telran.drugsintel.security.model.UserJWToken;
import telran.drugsintel.sign.dao.UserAccountRepository;
import telran.drugsintel.sign.dto.UserJWTokenDto;
import telran.drugsintel.sign.dto.UserRegisterDto;
import telran.drugsintel.sign.dto.UserResponseDto;
import telran.drugsintel.sign.dto.UserUpdateDto;
import telran.drugsintel.sign.dto.exceptions.UserNotFoundException;
import telran.drugsintel.sign.model.UserAccount;

@Service
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
	
	UserAccountRepository repository;
	ModelMapper modelMapper;

	@Override
	public UserJWTokenDto addUser(UserRegisterDto userRegisterDto) {
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		userAccount.setEmail(userRegisterDto.getEmail().toLowerCase());
		userAccount.setPassword(BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt()));
		repository.save(userAccount);
		UserJWToken token = new UserJWToken(userAccount.getUsername(), userAccount.getId());
		token.doGenerateToken();
		return modelMapper.map(token, UserJWTokenDto.class);
	}

	@Override
	public UserJWTokenDto loginUser(String login, String password) {
		Streamable<UserAccount> users;
		LocalDateTime lastUpdateDateTime;
		users = repository.findByUsername(login);
		lastUpdateDateTime = lastUpdatedAtUserAccount(users, password).orElse(null);
		if (lastUpdateDateTime == null) {
			users = repository.findByEmail(login.toLowerCase());
			lastUpdateDateTime = lastUpdatedAtUserAccount(users, password).orElseThrow(() -> new UserNotFoundException(login));
		}
		UserAccount userAccount;
		users = repository.findByUsernameAndUpdatedAt(login, lastUpdateDateTime);
		userAccount = checkPasswordUserAccount(users, password).orElse(null);
		if (userAccount == null) {
			users = repository.findByEmailAndUpdatedAt(login.toLowerCase(), lastUpdateDateTime);
			userAccount = checkPasswordUserAccount(users, password).orElseThrow(() -> new UserNotFoundException(login));
		}
		UserJWToken token = new UserJWToken(userAccount.getUsername(), userAccount.getId());
		token.doGenerateToken();
		return modelMapper.map(token, UserJWTokenDto.class);
	}

	private Optional<UserAccount> checkPasswordUserAccount(Streamable<UserAccount> users, String password) {
		return users.get()
				.filter(user -> BCrypt.checkpw(password, user.getPassword()))
				.findAny();
	}
	
	private Optional<LocalDateTime> lastUpdatedAtUserAccount(Streamable<UserAccount> users, String password) {
		return users.get()
				.filter(user -> BCrypt.checkpw(password, user.getPassword()))
				.map(user -> user.getUpdatedAt())
				.max((dateTime1, dateTime2) -> dateTime1.compareTo(dateTime2));
	}

	@Override
	public UserResponseDto getUser(String userId) {
		UserAccount userAccount = repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		return modelMapper.map(userAccount, UserResponseDto.class);
	}

	@Override
	public UserResponseDto removeUser(String userId, String username) {
		UserAccount userAccount = repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		if (username.equals(userAccount.getUsername())) {
			repository.deleteById(userId);
		}
		return modelMapper.map(userAccount, UserResponseDto.class);
	}

	@Override
	public UserResponseDto editUser(String userId, UserUpdateDto userUpdateDto) {
		UserAccount userAccount = repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		userAccount.setUsername(userUpdateDto.getUsername());
		userAccount.setEmail(userUpdateDto.getEmail());
		userAccount.setUpdatedAt(LocalDateTime.now());
		repository.save(userAccount);
		return modelMapper.map(userAccount, UserResponseDto.class);
	}

	@Override
	public void changePassword(String userId, String newPassword) {
		UserAccount userAccount = repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		userAccount.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
		userAccount.setUpdatedAt(LocalDateTime.now());
		repository.save(userAccount);
	}
	
}
