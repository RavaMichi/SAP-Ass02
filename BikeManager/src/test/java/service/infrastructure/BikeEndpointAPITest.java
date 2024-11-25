package service.infrastructure;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import service.domain.V2d;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class BikeEndpointAPITest {
    @Inject
    @Client("/") // Injects an HTTP client for testing
    HttpClient client;

    @Test
    public void testBikeUpdate() {
        var body = new BikeEndpointAPI.BikeUpdateRequest(20, new V2d(0,0));
        HttpRequest<?> request = HttpRequest.POST("/bikes/b1/update", body);

        BikeEndpointAPI.BikeUpdateResponse response = client.toBlocking().retrieve(request, BikeEndpointAPI.BikeUpdateResponse.class);
        assertNotNull(response);
    }
}
