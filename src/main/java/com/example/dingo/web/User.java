package com.example.dingo.web;

import lombok.ToString;
import lombok.Value;

@Value
public class User {
    String email;

    @ToString.Exclude
    String password;
}
