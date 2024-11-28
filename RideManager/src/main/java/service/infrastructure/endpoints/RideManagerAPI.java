package service.infrastructure.endpoints;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import service.application.RideManager;
import service.application.RideManagerListener;
import service.domain.RideManagerException;

@Controller("/ride-service")
public class RideManagerAPI {

    private final RideManager rideManager;

    public RideManagerAPI(RideManager rideManager) {
        this.rideManager = rideManager;
    }

    @Post("/connect")
    public HttpResponse<RMResponse> connect(
            @Header(HttpHeaders.AUTHORIZATION) String token,
            @Body RMRequest req
    ) {
        try {
            rideManager.connectUserToBike(req.userId(), req.bikeId());
            return HttpResponse.ok(new RMResponse("Successful connection", false));
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
            rideManager.disconnectUserFromBike(req.userId(), req.bikeId());
            return HttpResponse.ok(new RMResponse("Successful disconnection", false));
        } catch (RideManagerException e) {
            return HttpResponse.badRequest(new RMResponse(e.getMessage(), true));
        }
    }

    public record RMRequest(String userId, String bikeId) {}
    public record RMResponse(String message, boolean error) {}
}
