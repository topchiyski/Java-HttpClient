# Java-HttpClient

A small API testing project built on Java's built-in `java.net.http.HttpClient` (Java 11+), with [TestNG](https://testng.org/) for assertions and [SLF4J](https://www.slf4j.org/) for logging. The tests call the public [httpbingo.org](https://httpbingo.org) echo service, so no other HTTP or REST library is needed.

This repo is the companion example for my LinkedIn article: [Zero-Dependency API Testing in Java: Ditch Heavy Libraries for Built-In HttpClient + TestNG](https://www.linkedin.com/pulse/15-second-wait-before-test-1-even-starts-stoyan-topchiyski-qa1pf).

## Tech stack

| Component      | Version  |
|----------------|----------|
| Java           | 21       |
| Maven          | 3.x      |
| TestNG         | 7.10.2   |
| SLF4J (simple) | 2.0.16   |

## Project structure

```
Java-HttpClient/
├── pom.xml
└── src/test/java/
    ├── base/
    │   └── BaseApiTest.java        # Shared client, constants, and request/logging helpers
    ├── main/
    │   └── Main.java               # Runs all tests from a plain main method
    └── tests/
        ├── SimpleGetTest.java      # Minimal standalone GET example
        ├── CreateUserPostTest.java # POST with JSON body and Bearer token
        ├── HeaderCheckTest.java    # Reading and checking response headers
        └── StatusCheckTest.java    # Data-driven status code checks (@DataProvider)
```

## Core class: `BaseApiTest`

Test classes extend `BaseApiTest`, which provides:

- **`CLIENT`**: one `HttpClient` shared by the whole suite. It follows redirects (`Redirect.NORMAL`) and has a 5 second connect timeout.
- **`BASE_URL`**: `https://httpbingo.org`.
- **`requestTo(endpoint)`**: returns an `HttpRequest.Builder` with the full URL, a 10 second request timeout, and `Content-Type` / `Accept: application/json` headers already set.
- **`sendAndLog(request)`**: sends the request and logs the method, URI, status code, and response body.
- **Status constants**: `HTTP_OK`, `HTTP_UNAUTHORIZED`, `HTTP_NOT_FOUND`.

Example:

```java
public class HeaderCheckTest extends BaseApiTest {

    @Test
    public void testResponseHeaders() throws Exception {
        HttpRequest request = requestTo("/get").GET().build();
        HttpResponse<String> response = sendAndLog(request);

        String contentType = response.headers().firstValue("content-type").orElse("");
        Assert.assertTrue(contentType.contains("application/json"));
    }
}
```

## Tests

| Test class           | Endpoint(s)                                  | What it checks |
|----------------------|----------------------------------------------|----------------|
| `SimpleGetTest`      | `GET /get`                                   | Status is 200 and the body is echoed back. Does not use `BaseApiTest`, so it shows the bare client API. |
| `CreateUserPostTest` | `POST /post`                                 | Sends a JSON text block with a Bearer token, then checks for status 200 and that the payload is echoed. |
| `HeaderCheckTest`    | `GET /get`                                   | `Content-Type` response header contains `application/json`. |
| `StatusCheckTest`    | `GET /status/200`, `/status/401`, `/status/404` | Each endpoint returns the expected status code. Uses `@DataProvider` and `BodyHandlers.discarding()`. |

## Getting started

### Prerequisites

- JDK 21 or newer
- Maven 3.x
- Internet access (the tests call `https://httpbingo.org`)

### Run all tests with Maven

```bash
mvn test
```

### Run a single test class

```bash
mvn test -Dtest=StatusCheckTest
```

### Run from an IDE

In IntelliJ IDEA or Eclipse with the TestNG plugin, open any class under `src/test/java/tests` and run it directly.

### Run from a `main` method (`Main`)

`main.Main` runs all test classes through TestNG's programmatic API from a plain `main` method, then exits with a non-zero code if any test fails. Run it from your IDE, or from the command line:

```bash
mvn test-compile
mvn exec:java -Dexec.mainClass=main.Main -Dexec.classpathScope=test
```

## Logging

Logging uses `slf4j-simple` and goes to stderr. Each request logs the outgoing method and URI, and each response logs the status code and body, so test output shows the full HTTP exchange.

## Adding a new test

1. Create a class in `src/test/java/tests` that extends `BaseApiTest`.
2. Build a request with `requestTo("/your-endpoint")`.
3. Send it with `sendAndLog(request)`, or with `CLIENT.send(...)` if you need a different body handler.
4. Check the result with TestNG's `Assert` (argument order is `actual, expected`).

## License

This project is licensed under the [MIT License](LICENSE).
