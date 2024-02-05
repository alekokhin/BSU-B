package com.dev.mainbackend.repository.en;

import com.dev.mainbackend.models.en.ItemEn;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends MongoRepository<ItemEn, String> {
}
