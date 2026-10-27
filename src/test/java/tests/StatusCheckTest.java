package tests;

import base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Tests expected HTTP status codes for several echo-service endpoints.
 */
public class StatusCheckTest extends BaseApiTest {

    /**
     * Supplies endpoint paths and their expected HTTP status codes.
     *
     * @return rows containing an endpoint path and its expected status code
     */
    @DataProvider(name = "statusEndpoints")
    public Object[][] statusEndpoints() {
        return new Object[][]{
                {"/status/200", HTTP_OK},
                {"/status/401", HTTP_UNAUTHORIZED},
                {"/status/404", HTTP_NOT_FOUND}
        };
    }

    /**
     * Sends a GET request and verifies the endpoint's status code.
     *
     * @param endpoint       endpoint path to request
     * @param expectedStatus expected HTTP status code
     * @throws Exception if sending the HTTP request fails
     */
    @Test(dataProvider = "statusEndpoints")
    public void testEndpointStatusCodes(String endpoint, int expectedStatus) throws Exception {
        HttpRequest request = requestTo(endpoint).GET().build();

        // discarding() ignores the response body for faster status checks
        HttpResponse<Void> response = CLIENT.send(request, HttpResponse.BodyHandlers.discarding());

        LOGGER.info("Endpoint {} returned status {} (Expected: {})", endpoint, response.statusCode(), expectedStatus);

        Assert.assertEquals(response.statusCode(), expectedStatus);
    }
}
