package com.example.dingo.cqrs.login;


import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
public class Login {
    @AggregateIdentifier
    private String id;

    @CommandHandler
    public Login(final LoginCmd cmd) {
        if(!isValid(cmd))
            throw new IllegalArgumentException("Wrong credentials");

        AggregateLifecycle.apply(new LoginEvt(cmd.getId(), cmd.getEmail(), cmd.getPassword()));
    }

    @EventSourcingHandler
    public void on(final LoginEvt evt){
         this.id = evt.getId();
    }

    private boolean isValid(final LoginCmd cmd){
        return (!cmd.getPassword().isBlank());
    }
}
