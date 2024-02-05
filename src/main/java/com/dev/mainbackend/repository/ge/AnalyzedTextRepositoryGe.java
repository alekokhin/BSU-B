package com.dev.mainbackend.repository.ge;

import com.dev.mainbackend.models.ge.AnalyzedTextGe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyzedTextRepositoryGe extends MongoRepository<AnalyzedTextGe, String> {
}
