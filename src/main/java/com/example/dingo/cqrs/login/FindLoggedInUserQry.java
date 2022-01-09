package com.example.dingo.cqrs.login;

import lombok.Value;

@Value
public class FindLoggedInUserQry {
    private String id;
}
