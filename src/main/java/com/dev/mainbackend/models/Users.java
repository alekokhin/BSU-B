package com.dev.mainbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class Users {

    @Id
    private String _id; // Use Long instead of String for the id

    private String username;

    @JsonIgnore
    private String password;

    // Add a default constructor
    public Users() {
    }

    // Add a constructor with all fields except id
    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
