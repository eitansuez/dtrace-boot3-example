# Question:

_How do I propagate a request headers upstream to other services (for distributed tracing)?_

# Test:

Run the test `DemoApplicationTests`.

Alternatively, manually:

1. In one shell:

    ```shell
    gradle bootRun
    ```

1. In a separate shell:

    ```shell
    curl localhost:8080/ -H "x-request-id: world" 
    ```

The response should state: `Hello, requestId is: world`
But instead it states: `Hello, requestId is: null`
