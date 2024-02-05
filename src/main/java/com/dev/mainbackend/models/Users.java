package com.dev.mainbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String _id; // Use Long instead of String for the id

    @Column(unique = true)
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
