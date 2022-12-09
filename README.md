# Question:

_How do I propagate a request headers upstream to other services (for distributed tracing)?_

# Automated Test

Run the test `DemoApplicationTests`.

```shell
gradle test
```

# Manual test:

1. In one shell, run:

    ```shell
    gradle bootRun
    ```

1. Then, in a separate shell:

    ```shell
    curl localhost:8080/ -H "x-request-id: world" 
    ```

The response should state: `Hello, requestId is: world`
But instead it states: `Hello, requestId is: null`


Clearly, I'm doing something wrong.  But what?

