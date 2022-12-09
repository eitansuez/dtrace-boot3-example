package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
		String port = environment.getProperty("local.server.port");
		String rootUri = "http://localhost:"+port;
		String url = rootUri + "/hello";

		System.out.println("calling url "+url);

		return restTemplate.getForObject(url, String.class);
	}

	@GetMapping("/hello")
	public String helloEndpoint(@RequestHeader(value="x-request-id",required = false) String requestId) {
		return "Hello, requestId is: "+requestId;
	}
}
