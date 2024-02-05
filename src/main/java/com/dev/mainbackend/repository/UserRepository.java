package com.dev.mainbackend.repository;
import java.util.Optional;


import com.dev.mainbackend.models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, String> {
    Optional<Users> findByUsername(String username);

    Boolean existsByUsername(String username);

}
