package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

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
		String serverPort = environment.getProperty("local.server.port");
		String rootUri = "http://localhost:"+serverPort;
		return rootUri + "/hello";
	}

	@GetMapping("/manual")
	public String manualEndpoint(@RequestHeader(value="x-b3-traceid", required=false) String traceId) {
		String url = localHelloEndpointUrl();
		System.out.println("calling url "+url);

		RequestEntity<Void> request = RequestEntity.get(URI.create(url)).header("x-b3-traceid", traceId).build();
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		return response.getBody();
	}

	@GetMapping("/hello")
	public String helloEndpoint(@RequestHeader(value="x-b3-traceid", required=false) String traceId) {
		return "Hello, " + traceId;
	}
}
