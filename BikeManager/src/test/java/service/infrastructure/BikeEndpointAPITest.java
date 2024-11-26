package service.infrastructure;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import service.domain.V2d;
import service.infrastructure.endpoints.BMRequest;
import service.infrastructure.endpoints.BMResponse;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class BikeEndpointAPITest {
    @Inject
    @Client("/") // Injects an HTTP client for testing
    HttpClient client;

    @Test
    public void testBikeUpdate() {
        var body = new BMRequest("b1",20, new V2d(0,0));
        HttpRequest<?> request = HttpRequest.POST("/bikes/b1/update", body).header(HttpHeaders.AUTHORIZATION, "AUTHORIZED");

        assertThrows(Exception.class, () -> client.toBlocking().retrieve(request, BMResponse.class));
    }

    @Test
    public void testBikeUpdateNotAuthorized() {
        var body = new BMRequest("b1",20, new V2d(0,0));
        HttpRequest<?> request = HttpRequest.POST("/bikes/b1/update", body).header(HttpHeaders.AUTHORIZATION, "Wrong Token");

        assertThrows(Exception.class, () -> client.toBlocking().retrieve(request, BMResponse.class));
    }
}
