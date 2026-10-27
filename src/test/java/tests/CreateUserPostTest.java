package tests;

import base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Tests submitting a JSON payload to the echo service's POST endpoint.
 */
public class CreateUserPostTest extends BaseApiTest {

    /**
     * Posts a sample user payload and verifies that the service accepts and echoes it.
     *
     * @throws Exception if sending the HTTP request fails
     */
    @Test
    public void testCreateNewUser() throws Exception {
        // Clean multi-line JSON using Java Text Blocks
        String jsonBody = """
                {
                    "name": "Elena",
                    "role": "QA Engineer"
                }
                """;

        LOGGER.info("Preparing POST payload:\n{}", jsonBody);

        // Build POST request with a Bearer token and our JSON body
        HttpRequest request = requestTo("/post")
                .header("Authorization", "Bearer my-secret-test-token")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Sends the request and logs the status + body automatically
        HttpResponse<String> response = sendAndLog(request);

        // Verify status code and response JSON
        Assert.assertEquals(response.statusCode(), HTTP_OK);
        Assert.assertTrue(response.body().contains("Elena"));
    }
}
