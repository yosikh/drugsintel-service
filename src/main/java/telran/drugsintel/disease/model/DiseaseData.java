package telran.drugsintel.disease.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import telran.drugsintel.disease.dto.IntroDto;

@Getter
@EqualsAndHashCode(of = {"userId"})
@Document(collection = "dataintel")
public class DiseaseData {
	@Id
	String userId;
	IntroDto intro;
}
