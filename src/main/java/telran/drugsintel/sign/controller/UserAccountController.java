package telran.drugsintel.sign.controller;

import java.security.Principal;
import java.util.Base64;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import telran.drugsintel.sign.dto.UserJWTokenDto;
import telran.drugsintel.sign.dto.UserRegisterDto;
import telran.drugsintel.sign.dto.UserResponseDto;
import telran.drugsintel.sign.dto.UserUpdateDto;
import telran.drugsintel.sign.service.UserAccountService;

@RestController
@AllArgsConstructor
public class UserAccountController {

	UserAccountService userAccountService;
	
	@PostMapping("/register")
	public UserJWTokenDto regist(@RequestBody UserRegisterDto userRegisterDto) {
		return userAccountService.addUser(userRegisterDto);
	}
	
	@PostMapping("/login")
	public UserJWTokenDto login(@RequestHeader("Authorization") String token) {
		String login = getLoginFromToken(token);
		String password = getPasswordFromToken(token);
		return userAccountService.loginUser(login, password);
	}

	@GetMapping("/get")
	public UserResponseDto getUser(Principal principal) {
		return userAccountService.getUser(principal.getName());
	}
	
	@DeleteMapping("/user/{username}")
	public UserResponseDto deleteUser(Principal principal, @PathVariable String username) {
		return userAccountService.removeUser(principal.getName(), username);
	}
	
	@PutMapping("/update")
	public UserResponseDto updateUser(Principal principal, @RequestBody UserUpdateDto userUpdateDto) {
		return userAccountService.editUser(principal.getName(), userUpdateDto);
	}

	@PutMapping("/password")
	public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
		userAccountService.changePassword(principal.getName(), newPassword);
	}
	
	private String getLoginFromToken(String token) {
		String basicToken;
		try {
			basicToken = token.split(" ")[1];
		} catch (Exception e) {
			basicToken = token;
		}
		String decode = new String(Base64.getDecoder().decode(basicToken));
		String[] credentials = decode.split(":");
		return credentials[0];
	}
	
	private String getPasswordFromToken(String token) {
		String basicToken;
		try {
			basicToken = token.split(" ")[1];
		} catch (Exception e) {
			basicToken = token;
		}
		String decode = new String(Base64.getDecoder().decode(basicToken));
		String[] credentials = decode.split(":");
		return credentials[1];
	}
	
}
