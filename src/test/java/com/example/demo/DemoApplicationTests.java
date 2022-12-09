package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldPropagateHeader() throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-request-id", "world");

		RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, new URI("/"));
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isEqualTo("Hello, requestId is: world");
	}

	@Test
	void shouldManuallyPropagateHeader() throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-request-id", "world");

		RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, new URI("/manual"));
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isEqualTo("Hello, requestId is: world");
	}
}
