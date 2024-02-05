package com.dev.mainbackend.repository.ge;

import com.dev.mainbackend.models.ge.SymbolsGe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRepositoryGe extends MongoRepository<SymbolsGe, String> {
}
