package telran.drugsintel.disease.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import telran.drugsintel.disease.dao.DataIntelRepository;
import telran.drugsintel.disease.dto.IntroDto;
import telran.drugsintel.disease.dto.exceptions.DataNotFoundException;
import telran.drugsintel.disease.model.DiseaseData;

@Service
@AllArgsConstructor
public class DataintelServiceImpl implements DataintelService {

	DataIntelRepository repository;
	ModelMapper modelMapper;
	
	@Override
	public IntroDto getIntroData(String userId) {
		DiseaseData disease = repository.findById(userId).orElseThrow(() -> new DataNotFoundException(userId));
		return modelMapper.map(disease.getIntro(), IntroDto.class);
	}
		
}
