package com.example.dingo.cqrs.login;

import com.example.dingo.web.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginProjection {
    private final HashMap<String, User> users;
    private final QueryUpdateEmitter queryUpdateEmitter;


    @EventHandler
    public void on (final LoginEvt evt){
        val user = new User(evt.getEmail(), evt.getPassword());

        users.put(evt.getId(), user);

        queryUpdateEmitter.emit(FindLoggedInUserQry.class,
                query -> query.getId().equals(evt.getId()),
                user);

        queryUpdateEmitter.emit(AllUsersQry.class,
                qry -> true,
                user);
    }

    @QueryHandler
    public User fetch(FindLoggedInUserQry query) {
        System.out.println("fetch");
        System.out.println(query.toString());
        val result = users.get(query.getId());
        System.out.println(result);

        return result;
    }

    @QueryHandler
    private List<User> fetch(AllUsersQry qry){
        return new ArrayList<>(users.values());
    }
}
