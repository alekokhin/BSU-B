package com.dev.mainbackend.repository.en;

import com.dev.mainbackend.models.en.SymbolsEn;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRepository extends MongoRepository<SymbolsEn, String> {
}
