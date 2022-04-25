package telran.drugsintel.sign.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6732306100542141591L;
	
	public UserExistsException(String email) {
		super("User " + email + " exists");
	}

}
