package service.infrastructure;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.*;
import service.application.BikeEndpoint;
import service.domain.BikeOperationException;
import service.domain.V2d;

/**
 * Adapter for BikeEndpoint. Exposes a REST API for EBikes to use.
 */
@Controller("/bikes")
public class BikeEndpointAPI {

    private BikeEndpoint bikeEndpoint;

    public BikeEndpointAPI(BikeEndpoint bikeEndpoint) {
        this.bikeEndpoint = bikeEndpoint;
    }

    @Post("/{id}/update")
    public BikeUpdateResponse updateBikeState(@Body BikeUpdateRequest req, String id) {
        try {
            bikeEndpoint.updateBike(id, req.battery(), req.position());
            return new BikeUpdateResponse("EBike " + id + " updated", false);
        } catch (BikeOperationException e) {
            return new BikeUpdateResponse(e.getMessage(), true);
        }
    }

    /**
     * Body of a bike update request
     * @param battery
     * @param position
     */
    @Introspected
    public record BikeUpdateRequest(
            int battery,
            V2d position
    ) {}

    /**
     * Response to a bike update request
     * @param message
     * @param error
     */
    @Introspected
    public record BikeUpdateResponse(
            String message,
            boolean error
    ) {}
}
