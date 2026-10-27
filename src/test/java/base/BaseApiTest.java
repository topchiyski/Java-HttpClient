package base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Provides shared HTTP client configuration and request helpers for API tests.
 */
public class BaseApiTest {

    /** Logger shared by API test subclasses. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseApiTest.class);

    /** Base URL for the public HTTP echo service used by the tests. */
    protected static final String BASE_URL = "https://httpbingo.org";

    // Clear timeout constants instead of magic numbers
    /** Maximum time allowed to establish an HTTP connection. */
    protected static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    /** Maximum time allowed for an individual HTTP request. */
    protected static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    // Reusable HTTP status code constants for clean assertions
    /** HTTP status code indicating a successful request. */
    protected static final int HTTP_OK = 200;
    /** HTTP status code indicating that authentication is required. */
    protected static final int HTTP_UNAUTHORIZED = 401;
    /** HTTP status code indicating that the requested resource was not found. */
    protected static final int HTTP_NOT_FOUND = 404;

    // Create ONE shared client for your whole test suite
    /** Shared client configured to follow normal redirects and use the connection timeout. */
    protected static final HttpClient CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL) // Automatically follow 301/302 redirects
            .connectTimeout(CONNECT_TIMEOUT)             // Fail fast if the server is down
            .build();

    // Helper method that pre-fills the URL, timeout, and common headers
    /**
     * Creates a request builder with the service URL, request timeout, and JSON headers.
     *
     * @param endpoint endpoint path appended to {@link #BASE_URL}
     * @return a configured request builder for the endpoint
     */
    protected HttpRequest.Builder requestTo(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .timeout(REQUEST_TIMEOUT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    // Helper method that sends the request and automatically logs the details
    /**
     * Sends a request using the shared client, logs the request and response, and reads the body as a string.
     *
     * @param request request to send
     * @return the response with its body decoded as a string
     * @throws Exception if sending the request is interrupted or fails
     */
    protected HttpResponse<String> sendAndLog(HttpRequest request) throws Exception {
        LOGGER.info("--> Sending {} to {}", request.method(), request.uri());

        HttpResponse<String> response = CLIENT.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        LOGGER.info("<-- Received Status: {} from {}", response.statusCode(), request.uri());
        LOGGER.info("<-- Response Body:\n{}", response.body());
        return response;
    }
}
