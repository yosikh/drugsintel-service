package telran.drugsintel.disease.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.drugsintel.disease.model.DiseaseData;

public interface DataIntelRepository extends MongoRepository<DiseaseData, String> {

}
