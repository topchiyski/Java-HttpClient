package tests;

import base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StatusCheckTest extends BaseApiTest {

    @DataProvider(name = "statusEndpoints")
    public Object[][] statusEndpoints() {
        return new Object[][] {
                { "/status/200", HTTP_OK },
                { "/status/401", HTTP_UNAUTHORIZED },
                { "/status/404", HTTP_NOT_FOUND }
        };
    }

    @Test(dataProvider = "statusEndpoints")
    public void testEndpointStatusCodes(String endpoint, int expectedStatus) throws Exception {
        HttpRequest request = requestTo(endpoint).GET().build();

        // discarding() ignores the response body for faster status checks
        HttpResponse<Void> response = CLIENT.send(
                request,
                HttpResponse.BodyHandlers.discarding()
        );

        LOGGER.info("Endpoint {} returned status {} (Expected: {})",
                endpoint, response.statusCode(), expectedStatus);

        Assert.assertEquals(response.statusCode(), expectedStatus);
    }
}