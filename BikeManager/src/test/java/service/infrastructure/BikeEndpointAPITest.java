package service.infrastructure;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
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
        var body = new BMRequest("b1",20, new V2d(0,0));
        HttpRequest<?> request = HttpRequest.POST("/bikes/b1/update", body).header(HttpHeaders.AUTHORIZATION, "AUTHORIZED");

        BMResponse response = client.toBlocking().retrieve(request, BMResponse.class);
        assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void testBikeUpdateNotAuthorized() {
        var body = new BMRequest("b1",20, new V2d(0,0));
        HttpRequest<?> request = HttpRequest.POST("/bikes/b1/update", body).header(HttpHeaders.AUTHORIZATION, "Wrong Token");

        assertThrows(Exception.class, () -> client.toBlocking().retrieve(request, BMResponse.class));
    }
}
