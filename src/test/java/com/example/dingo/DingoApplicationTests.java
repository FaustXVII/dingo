package com.example.dingo;

import com.example.dingo.cqrs.login.Login;
import com.example.dingo.cqrs.login.LoginCmd;
import com.example.dingo.cqrs.login.LoginEvt;
import lombok.val;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DingoApplicationTests {
	private FixtureConfiguration<Login> fixture;

	@BeforeEach
	public void setUp() {
		fixture = new AggregateTestFixture<>(Login.class);
	}

	@Test
	void contextLoads() {
		val id = "1";
		val email = "dingo@zoo.com";
		val password = "Dont tell";

		fixture.givenNoPriorActivity()
				.when(new LoginCmd(id, email, password))
				.expectEvents(new LoginEvt(id, email, password));
	}

}
