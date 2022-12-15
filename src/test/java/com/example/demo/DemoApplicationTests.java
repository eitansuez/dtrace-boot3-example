package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureObservability
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldPropagateBaggage() {
		testForEndpoint("/");
	}

	@Test
	void shouldManuallyPropagateBaggage() {
		testForEndpoint("/manual");
	}

	private void testForEndpoint(String endpoint) {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create(endpoint))
				.header("x-request-id", "world")
				.build();
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isEqualTo("Hello, world");
	}

}
