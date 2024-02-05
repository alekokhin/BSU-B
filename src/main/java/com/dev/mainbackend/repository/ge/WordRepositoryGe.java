package com.dev.mainbackend.repository.ge;

import com.dev.mainbackend.models.ge.WordsGe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepositoryGe extends MongoRepository<WordsGe, String> {
}
