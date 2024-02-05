package com.dev.mainbackend.repository.en;

import com.dev.mainbackend.models.en.StringsEn;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StringRepository extends MongoRepository<StringsEn,String> {
}
