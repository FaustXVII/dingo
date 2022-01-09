package com.example.dingo.web;

import com.example.dingo.cqrs.login.FindLoggedInUserQry;
import com.example.dingo.cqrs.login.LoginCmd;
import com.example.dingo.cqrs.login.AllUsersQry;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class Endpoints {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping("/logout")
    public CompletableFuture<Void> logout(@RequestBody final User user){
        val id = UUID.randomUUID().toString();

        //TODO: make this a logout
        val cmd = new LoginCmd(id, user.getEmail(), user.getPassword());

       return commandGateway.send(cmd);
    }

//    @PostMapping(value = "/login", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<User> login(@RequestBody final User user){
//        val id = UUID.randomUUID().toString();
//        val cmd = new LoginCmd(id, user.getEmail(), user.getPassword());
//        val responseTypes = ResponseTypes.instanceOf(User.class);
//
//        try (val qryResult = queryGateway.subscriptionQuery(new FindLoggedInUserQry(id), responseTypes, responseTypes)) {
//            commandGateway.sendAndWait(cmd);
//
//            return qryResult.updates();
//        }
//    }

//    @PostMapping(value = "/login", produces = MediaType.TEXT_EVENT_STREAM_VALUE, consumes="application/stream+json")
//    public Flux<User> login2(@RequestBody final User user){
//        val id = UUID.randomUUID().toString();
//
//        // Command
//        commandGateway.sendAndWait(new LoginCmd(id, user.getEmail(), user.getPassword()));
//
//        // Query
//        val responseTypes = ResponseTypes.instanceOf(User.class);
//        val qryResult = queryGateway.subscriptionQuery(new FindLoggedInUserQry(id), responseTypes, responseTypes);
//
//        val initialResult = qryResult.initialResult();
//        System.out.println("result: "+initialResult);
//        return Flux.concat(initialResult, qryResult.updates());
//    }

    @GetMapping(value = "/allUsers", produces = MediaType.TEXT_EVENT_STREAM_VALUE, consumes="application/stream+json")
    public Flux<User> getAllUsers(){
        // Query
        val qryResult = queryGateway.subscriptionQuery(new AllUsersQry(), ResponseTypes.multipleInstancesOf(User.class), ResponseTypes.instanceOf(User.class));

        Flux<User> initialResult = qryResult.initialResult().flatMapMany(Flux::fromIterable);
//        return Flux.concat(initialResult, qryResult.updates());
        return qryResult.updates();
    }


    @PostMapping(value = "/loginFuture")
    public CompletableFuture<User> loginFuture(@RequestBody final User user){
        System.out.println("login Future");
        val id = UUID.randomUUID().toString();

        // Command
        commandGateway.sendAndWait(new LoginCmd(id, user.getEmail(), user.getPassword()));

        // Query
        val responseTypes = ResponseTypes.instanceOf(User.class);
        val response = queryGateway.query(new FindLoggedInUserQry(id), responseTypes);

        System.out.println("return: "+response);
        return response;
    }


    @PostMapping(value = "/loginMake")
    public String loginMake(@RequestBody final User user){
        val id = UUID.randomUUID().toString();

        // Command
        commandGateway.sendAndWait(new LoginCmd(id, user.getEmail(), user.getPassword()));

        return id;
    }

    @PostMapping(value = "/loginGet")
    public CompletableFuture<User> loginGet(@RequestBody final String id){
        System.out.println("login Get");
        // Query
        val responseTypes = ResponseTypes.instanceOf(User.class);

        val response = queryGateway.query(new FindLoggedInUserQry(id), responseTypes);

        System.out.println("return: "+response);
        return response;
    }

}
