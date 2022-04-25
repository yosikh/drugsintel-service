package telran.drugsintel.sign.service;

import telran.drugsintel.sign.dto.UserJWTokenDto;
import telran.drugsintel.sign.dto.UserRegisterDto;
import telran.drugsintel.sign.dto.UserResponseDto;
import telran.drugsintel.sign.dto.UserUpdateDto;

public interface UserAccountService {

	UserJWTokenDto addUser(UserRegisterDto userRegisterDto);
	
	UserJWTokenDto loginUser(String email, String password);
	
	UserResponseDto getUser(String userId);
	
	UserResponseDto removeUser(String userId, String username);
	
	UserResponseDto editUser(String userId, UserUpdateDto userUpdateDto);
	
	void changePassword(String userId, String newPassword);
	
}
