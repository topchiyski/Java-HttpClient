package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SimpleGetTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleGetTest.class);

    private static final String GET_URL = "https://httpbingo.org/get";
    private static final int HTTP_OK = 200;

    @Test
    public void testGetUserStatus() throws Exception {
        // 1. Create the client
        HttpClient client = HttpClient.newHttpClient();

        // 2. Build the GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_URL))
                .GET()
                .build();

        LOGGER.info("Sending GET request to: {}", GET_URL);

        // 3. Send the request and get the response body as a String
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        LOGGER.info("Received Status Code: {}", response.statusCode());
        LOGGER.info("Response Body:\n{}", response.body());

        // 4. Verify with TestNG Assert (actual, expected)
        Assert.assertEquals(response.statusCode(), HTTP_OK);
        Assert.assertTrue(response.body().contains("httpbingo.org"));
    }
}
