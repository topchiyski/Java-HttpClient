package tests;

import base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HeaderCheckTest extends BaseApiTest {

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