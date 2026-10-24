package base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class BaseApiTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseApiTest.class);

    protected static final String BASE_URL = "https://httpbingo.org";

    // Clear timeout constants instead of magic numbers
    protected static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    protected static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    // Reusable HTTP status code constants for clean assertions
    protected static final int HTTP_OK = 200;
    protected static final int HTTP_UNAUTHORIZED = 401;
    protected static final int HTTP_NOT_FOUND = 404;

    // Create ONE shared client for your whole test suite
    protected static final HttpClient CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL) // Automatically follow 301/302 redirects
            .connectTimeout(CONNECT_TIMEOUT)             // Fail fast if the server is down
            .build();

    // Helper method that pre-fills the URL, timeout, and common headers
    protected HttpRequest.Builder requestTo(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .timeout(REQUEST_TIMEOUT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    // Helper method that sends the request and automatically logs the details
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
