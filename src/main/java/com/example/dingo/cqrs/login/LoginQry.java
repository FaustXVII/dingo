package com.example.dingo.cqrs.login;

import lombok.ToString;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class LoginQry {
    @TargetAggregateIdentifier
    String id;

    String email;

    @ToString.Exclude
    String password;
}
