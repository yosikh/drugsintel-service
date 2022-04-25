package telran.drugsintel.disease.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import telran.drugsintel.disease.dto.IntroDto;
import telran.drugsintel.disease.service.DataintelService;

@RestController
@AllArgsConstructor
public class DataintelController {

	DataintelService dataintelService;
	
	@GetMapping("/dashboard/intro")
	public IntroDto intro(Principal principal) {
		return dataintelService.getIntroData(principal.getName());
	}
	
}
