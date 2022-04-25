package telran.drugsintel.sign.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDto {
	String username;
	String email;
	String role;
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDateTime updatedAt;
}
