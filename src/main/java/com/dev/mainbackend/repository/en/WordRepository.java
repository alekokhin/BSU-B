package com.dev.mainbackend.repository.en;

import com.dev.mainbackend.models.en.WordsEn;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends MongoRepository<WordsEn, String> {
}
