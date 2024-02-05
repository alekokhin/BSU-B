package com.dev.mainbackend.repository.ge;

import com.dev.mainbackend.models.ge.ItemGe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryGe extends MongoRepository<ItemGe, String> {
}
