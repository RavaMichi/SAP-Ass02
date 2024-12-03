package app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.*;

public class ApiGateway extends AbstractVerticle {

    // Map to store service routing information
    private final Map<String, RouteData> serviceRoutes = new HashMap<>();
    private final Vertx vertx;

    public ApiGateway(Vertx vertx) {
        this.vertx = vertx;
    }

    private void initializeRoutes() {
        // bike manager
        serviceRoutes.put("/bikes", new RouteData(8080, "bike-manager", "/bikes"));
        // account manager
        serviceRoutes.put("/users", new RouteData(8080, "account-manager", "/users"));
        // ride manager
        serviceRoutes.put("/ride-service", new RouteData(8080, "ride-manager", "/ride-service"));
        // auth server
        serviceRoutes.put("/auth", new RouteData(8080, "auth-service", "/auth"));
    }

    @Override
    public void start() {
        initializeRoutes();

        // Set up Vert.x WebClient
        WebClientOptions options = new WebClientOptions().setFollowRedirects(true);
        WebClient client = WebClient.create(vertx, options);

        // Set up HTTP server and router
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()); // Handle request bodies

        router.route().handler(ctx -> {
            HttpServerRequest request = ctx.request();
            HttpServerResponse response = ctx.response();

            // Match the incoming request path
            var target = computeUrl(request.path());

            if (target.isPresent()) {
                // forwarding
                var requestBuilder = switch (request.method().name().toUpperCase()) {
                    case "GET" -> client.get(target.get().port(), target.get().host(), target.get().uri())
                            .putHeaders(request.headers())
                            .send();
                    case "POST" -> client.post(target.get().port(), target.get().host(), target.get().uri())
                            .putHeaders(request.headers())
                            .sendBuffer(ctx.getBody());
                    default -> client.request(request.method(), target.get().port(), target.get().host(), target.get().uri())
                            .putHeaders(request.headers())
                            .send();
                };
                requestBuilder.onSuccess(res -> {
                    response.setStatusCode(res.statusCode());
                    response.headers().setAll(res.headers());
                    response.end(res.bodyAsBuffer());
                }).onFailure(err -> {
                    response.setStatusCode(500).end(err.getMessage());
                });
            } else {
                // no matching service route
                response.setStatusCode(404).end("Service not found.");
            }
        });

        // Start the HTTP server
        vertx.createHttpServer().requestHandler(router).listen(8080, http -> {
            if (http.succeeded()) {
                System.out.println("API Gateway started on port 8080");
            } else {
                System.err.println("Failed to start API Gateway: " + http.cause().getMessage());
            }
        });
    }
    // try to compute the new path to forward
    private Optional<RouteData> computeUrl(String path) {
        return serviceRoutes.keySet().stream()
                .filter(path::startsWith)
                .findAny()
                .map(route -> {
                    RouteData r = serviceRoutes.get(route);
                    return new RouteData(r.port(), r.host(), path.replaceFirst(route, r.uri()));
                });
    }
}
