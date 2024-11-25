package service.infrastructure;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Controller;

/**
 * Health check pattern implementation
 */
@Controller("/health")
public class BikeManagerHealthCheck {
    /**
     * Health check API - Verify the system integrity
     * @return the check result
     */
    @Get
    public HttpResponse<String> checkHealth() {
        return HttpResponse.ok("successful-check");
    }
}
