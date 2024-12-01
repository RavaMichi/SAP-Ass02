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

import java.util.HashMap;
import java.util.Map;

public class ApiGateway extends AbstractVerticle {

    // Map to store service routing information
    private final Map<String, String> serviceRoutes = new HashMap<>();
    private final Vertx vertx;

    public ApiGateway(Vertx vertx) {
        this.vertx = vertx;
    }

    private void initializeRoutes() {
        // bike manager
        serviceRoutes.put("/bikes", "http://bike-manager:8080/bikes");
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
            String targetUrl = serviceRoutes.get(request.path());

            if (targetUrl != null) {
                // forwarding
                var requestBuilder = switch (request.method().name().toUpperCase()) {
                    case "GET" -> client.get(targetUrl)
                            .putHeaders(request.headers())
                            .send();
                    case "POST" -> request.body().flatMap(
                            b -> client.post(targetUrl)
                                    .putHeaders(request.headers())
                                    .sendBuffer(b)
                    );
                    default ->
                            throw new IllegalStateException("Unexpected value: " + request.method().name().toUpperCase());
                };
                requestBuilder.onSuccess(res -> {
                    response.setStatusCode(res.statusCode());
                    response.headers().setAll(res.headers());
                    response.end(res.bodyAsBuffer());
                }).onFailure(err -> {
                    response.setStatusCode(500).end("Failed to contact service.");
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
}
