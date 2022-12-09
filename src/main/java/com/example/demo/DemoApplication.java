package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@Configuration
class Config {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}

@RestController
class MyController {

	private final RestTemplate restTemplate;
	private final Environment environment;

	public MyController(RestTemplate restTemplate, Environment environment) {
		this.restTemplate = restTemplate;
		this.environment = environment;
	}

	@GetMapping("/")
	public String mainEndpoint() {
		String url = localHelloEndpointUrl();
		System.out.println("calling url "+url);
		return restTemplate.getForObject(url, String.class);
	}

	private String localHelloEndpointUrl() {
		String port = environment.getProperty("local.server.port");
		String rootUri = "http://localhost:"+port;
		return rootUri + "/hello";
	}

	@GetMapping("/manual")
	public String manualEndpoint(@RequestHeader(value="x-request-id", required=false) String requestId) throws URISyntaxException {
		String url = localHelloEndpointUrl();
		System.out.println("calling url "+url);

		HttpHeaders headers = new HttpHeaders();
		headers.set("x-request-id", requestId);
		RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, new URI(url));
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		return response.getBody();
	}

	@GetMapping("/hello")
	public String helloEndpoint(@RequestHeader(value="x-request-id", required=false) String requestId) {
		return "Hello, requestId is: "+requestId;
	}
}
