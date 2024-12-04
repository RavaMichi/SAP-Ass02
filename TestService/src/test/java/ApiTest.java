import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * End-to-End Test. Works only if the service is already online
 */
@ExtendWith(VertxExtension.class)
public class ApiTest {

    private final String username = "test user";
    private final String password = "test password";
    private final String bikeId = "THIS BIKE DOES NOT EXIST";

    WebClient webClient;

    private Future<String> fetchTestUserToken() {
        JsonObject user = new JsonObject()
                .put("username", username)
                .put("password", password);

        return webClient.post(8080, "localhost", "/auth/login")
                .putHeader("Content-type", "application/json")
                .sendJsonObject(user)
                .flatMap(res -> {
                    if (res.statusCode() == 200) {
                        return Future.succeededFuture(res.bodyAsString());
                    } else {
                        return webClient.post(8080, "localhost", "/auth/register")
                                .putHeader("Content-type", "application/json")
                                .sendJsonObject(user)
                                .map(HttpResponse::bodyAsString);
                    }
                });
    }

    @Test
    void testCannotRIdeIfBikeDoesNotExist(Vertx vertx, VertxTestContext testContext) {
        webClient = WebClient.create(vertx);

        var token = fetchTestUserToken();

        JsonObject requestWrongRide = new JsonObject()
                .put("userId", username)
                .put("bikeId", bikeId);

        token.onSuccess(t -> {
            webClient.post(8080, "localhost", "/ride-service/connect")
                    .putHeader("Content-type", "application/json")
                    .sendJsonObject(requestWrongRide)
                    .onComplete(res -> {
                        if (res.failed()) {
                            testContext.failNow(res.cause());
                        } else {
                            assertEquals(400, res.result().statusCode());
                            testContext.completeNow();
                        }
                    });
        });
    }
}
