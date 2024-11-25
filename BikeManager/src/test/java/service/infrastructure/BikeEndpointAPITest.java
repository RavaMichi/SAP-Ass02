package service.infrastructure;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import service.domain.V2d;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class BikeEndpointAPITest {
    @Inject
    @Client("/") // Injects an HTTP client for testing
    HttpClient client;

    @Test
    public void testBikeUpdate() {
        var body = new BikeEndpointAPI.BikeUpdateRequest(20, new V2d(0,0));
        HttpRequest<?> request = HttpRequest.POST("/bikes/b1/update", body).header(HttpHeaders.AUTHORIZATION, "Bearer: bike b1");

        BikeEndpointAPI.BikeUpdateResponse response = client.toBlocking().retrieve(request, BikeEndpointAPI.BikeUpdateResponse.class);
        assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void testBikeUpdateNotAuthorized() {
        var body = new BikeEndpointAPI.BikeUpdateRequest(20, new V2d(0,0));
        HttpRequest<?> request = HttpRequest.POST("/bikes/b1/update", body).header(HttpHeaders.AUTHORIZATION, "Wrong Token");

        BikeEndpointAPI.BikeUpdateResponse response = client.toBlocking().retrieve(request, BikeEndpointAPI.BikeUpdateResponse.class);
        assertNotNull(response);
        assertTrue(response.error());
        System.out.println(response);
    }
}
