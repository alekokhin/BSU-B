package com.dev.mainbackend.repository.ge;

import com.dev.mainbackend.models.ge.StringsGe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StringRepositoryGe extends MongoRepository<StringsGe, String> {
}
