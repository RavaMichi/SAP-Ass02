package service.infrastructure.endpoints;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import service.application.RideManager;
import service.domain.RideManagerException;
import service.infrastructure.auth.AuthChecker;

@Controller("/ride-service")
public class RideManagerAPI {

    private final RideManager rideManager;
    private final AuthChecker authChecker;

    public RideManagerAPI(RideManager rideManager, AuthChecker authChecker) {
        this.rideManager = rideManager;
        this.authChecker = authChecker;
    }

    @Post("/connect")
    public HttpResponse<RMResponse> connect(
            @Header(HttpHeaders.AUTHORIZATION) String token,
            @Body RMRequest req
    ) {
        try {
            if (authChecker.isAuthorized(token)) {
                rideManager.connectUserToBike(req.userId(), req.bikeId());
                return HttpResponse.ok(new RMResponse("Successful connection", false));
            } else {
                return HttpResponse.unauthorized();
            }
        } catch (RideManagerException e) {
            return HttpResponse.badRequest(new RMResponse(e.getMessage(), true));
        }
    }

    @Post("/disconnect")
    public HttpResponse<RMResponse> disconnect(
            @Header(HttpHeaders.AUTHORIZATION) String token,
            @Body RMRequest req
    ) {
        try {
            if (authChecker.isAuthorized(token)) {
                rideManager.disconnectUserFromBike(req.userId(), req.bikeId());
                return HttpResponse.ok(new RMResponse("Successful disconnection", false));
            } else {
                return HttpResponse.unauthorized();
            }
        } catch (RideManagerException e) {
            return HttpResponse.badRequest(new RMResponse(e.getMessage(), true));
        }
    }

    public record RMRequest(String userId, String bikeId) {}
    public record RMResponse(String message, boolean error) {}
}
