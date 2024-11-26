package service.infrastructure;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import service.application.BikeDatabase;
import service.application.MockBikeDatabase;
import service.domain.V2d;
import service.infrastructure.db.FileBikeDatabase;
import service.infrastructure.endpoints.BMRequest;
import service.infrastructure.endpoints.BMResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * End-to-End testing
 */
@MicronautTest
public class BikeManagerEndToEndTests {
    @Inject
    @Client("/") // Injects an HTTP client for testing
    HttpClient client;

    @MockBean(BikeDatabase.class)
    BikeDatabase mockDatabase() {
        return new MockBikeDatabase();
    }

    @Test
    public void testAllBikes() {
        HttpRequest<?> allBikes = HttpRequest.GET("/metrics/total-bikes");
        int n_bikes = client.toBlocking().retrieve(allBikes, int.class);

        HttpRequest<?> addBike = HttpRequest.POST("/bikes/add", new BMRequest("b1", 20, new V2d(0,0))).header(HttpHeaders.AUTHORIZATION, "AUTHORIZED");
        client.toBlocking().retrieve(addBike, BMResponse.class);

        int updated_n_bikes = client.toBlocking().retrieve(allBikes, int.class);
        assertEquals(n_bikes + 1, updated_n_bikes);
    }
}
