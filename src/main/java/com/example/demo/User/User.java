package com.example.demo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class User {

    private Integer id;
    private String username;
    private String email;
    private String passwordHash;
    private Timestamp createdAt;
}