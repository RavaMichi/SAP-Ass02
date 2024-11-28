package service.infrastructure.endpoints;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import service.application.AccountManager;
import service.domain.RideManagerException;
import service.infrastructure.Config;

import java.util.Optional;

/**
 * Adapter for AccountManager. Sends http requests to the account manager service
 */
@Singleton
public class AccountManagerClient implements AccountManager {
    @Inject
    @Client("http://account-manager:8080/users")
    private HttpClient httpClient;

    @Override
    public boolean doesUserExist(String username) {
        return getUserInfo(username).isPresent();
    }

    @Override
    public boolean doesUserHaveEnoughCredits(String username, int amount) {
        return getUserInfo(username).map( u -> u.credits() >= amount ).orElse(false);
    }

    private Optional<UserInfo> getUserInfo(String username) {
        try {
            HttpRequest<?> request = HttpRequest.GET("/" + username).header(HttpHeaders.AUTHORIZATION, Config.serviceToken);
            // send request and wait
            return Optional.ofNullable(httpClient.toBlocking().retrieve(request, UserInfo.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void deductCreditsFromUser(String username, int amount) {
        try {
            HttpRequest<?> request = HttpRequest.POST("/" + username + "/remove-credit", new UserInfo(username, amount)).header(HttpHeaders.AUTHORIZATION, Config.serviceToken);
            // send request and wait
            httpClient.toBlocking().retrieve(request, String.class);

        } catch (Exception e) {
            throw new RideManagerException("Error in deducting credit from user " + username);
        }
    }

    @Introspected
    private record UserInfo(String username, int credits) {}
}
