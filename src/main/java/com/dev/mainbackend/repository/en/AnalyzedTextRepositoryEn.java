package com.dev.mainbackend.repository.en;

import com.dev.mainbackend.models.en.AnalyzedTextEn;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyzedTextRepositoryEn extends MongoRepository<AnalyzedTextEn, String> {
}
