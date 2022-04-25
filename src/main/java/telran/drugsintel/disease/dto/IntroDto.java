package telran.drugsintel.disease.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntroDto {
	String title;
	@Singular
	Set<String> titleSets;
	String paragraph;
	@Singular
	Set<String> paragraphSets;
}
