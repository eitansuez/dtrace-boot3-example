package com.example.demo;

import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class MyController {

  private final RestTemplate restTemplate;

  private final Environment environment;

  public MyController(RestTemplate restTemplate, Environment environment) {
    this.restTemplate = restTemplate;
    this.environment = environment;
  }

  @GetMapping("/")
  public String mainEndpoint() {
    String url = localHelloEndpointUrl();
    System.out.println("calling url " + url);
    return restTemplate.getForObject(url, String.class);
  }

  private String localHelloEndpointUrl() {
    String serverPort = environment.getProperty("local.server.port");
    String rootUri = "http://localhost:" + serverPort;
    return rootUri + "/hello";
  }

  @GetMapping("/manual")
  public String manualEndpoint(@RequestHeader(value = "x-request-id", required = false) String traceId) {
    String url = localHelloEndpointUrl();
    System.out.println("calling url " + url);

    RequestEntity<Void> request = RequestEntity.get(URI.create(url)).header("x-request-id", traceId).build();
    ResponseEntity<String> response = restTemplate.exchange(request, String.class);
    return response.getBody();
  }

  @GetMapping("/hello")
  public String helloEndpoint(@RequestHeader(value = "x-request-id", required = false) String xRequestId) {
    return "Hello, " + xRequestId;
  }
}
