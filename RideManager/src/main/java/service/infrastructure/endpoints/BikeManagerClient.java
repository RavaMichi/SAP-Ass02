package service.infrastructure.endpoints;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import service.application.BikeManager;
import service.infrastructure.Config;

import java.util.Optional;

/**
 * Adapter for BikeManager. sends API calls to th e bike manager service
 */
@Singleton
public class BikeManagerClient implements BikeManager {

    private final HttpClient httpClient;
    public BikeManagerClient(@Client("http://bike-manager:8080/bikes") HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public boolean doesBikeExist(String bikeId) {
        try {
            HttpRequest<?> request = HttpRequest.GET("/" + bikeId).header(HttpHeaders.AUTHORIZATION, Config.serviceToken);
            // send request and wait
            Optional<?> result = httpClient.toBlocking().retrieve(request, Optional.class);
            return result.isPresent();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
