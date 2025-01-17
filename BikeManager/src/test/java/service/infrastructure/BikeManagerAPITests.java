package service.infrastructure;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Component testing
 */
@MicronautTest
public class BikeManagerAPITests {
    @Inject
    @Client("/") // Injects an HTTP client for testing
    HttpClient client;

    @Test
    public void testAllBikes() {
        HttpRequest<?> request = HttpRequest.GET("/bikes").header(HttpHeaders.AUTHORIZATION, "AUTHORIZED");

        var response = client.toBlocking().retrieve(request, List.class);
        assertNotNull(response);
        System.out.println(response);
    }
}
