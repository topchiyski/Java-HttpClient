package tests;

import base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Tests response headers returned by the echo service.
 */
public class HeaderCheckTest extends BaseApiTest {

    /**
     * Verifies that the GET endpoint returns a JSON content type.
     *
     * @throws Exception if sending the HTTP request fails
     */
    @Test
    public void testResponseHeaders() throws Exception {
        HttpRequest request = requestTo("/get").GET().build();

        HttpResponse<String> response = sendAndLog(request);

        // Grab a header value (returns an empty String if missing)
        String contentType = response.headers()
                .firstValue("content-type")
                .orElse("");

        LOGGER.info("Verified Content-Type header: {}", contentType);

        Assert.assertTrue(contentType.contains("application/json"));
    }
}
