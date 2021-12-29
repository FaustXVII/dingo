package com.example.dingo.web;

import com.example.dingo.cqrs.login.LoginCmd;
import lombok.val;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class Endpoints {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public Endpoints(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/login")
    public CompletableFuture<Void> login(@RequestBody final User user){
        val id = UUID.randomUUID().toString();
        val cmd = new LoginCmd(id, user.getEmail(), user.getPassword());

       return commandGateway.send(cmd);
    }
}
