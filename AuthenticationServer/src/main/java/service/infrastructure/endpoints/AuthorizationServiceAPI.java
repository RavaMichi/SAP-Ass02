package service.infrastructure.endpoints;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import service.application.AuthenticationService;
import service.domain.AuthenticationException;

@Controller("/auth")
public class AuthorizationServiceAPI {

    private final AuthenticationService authenticationService;
    public AuthorizationServiceAPI(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Post("/login")
    @Produces(MediaType.TEXT_PLAIN)
    public HttpResponse<String> login(@Body LoginRequest req) {
        try {
            var token = authenticationService.authenticate(req.username(), req.password());
            return HttpResponse.ok(token);
        } catch (AuthenticationException e) {
            return HttpResponse.badRequest();
        }
    }

    @Post("/register")
    @Produces(MediaType.TEXT_PLAIN)
    public HttpResponse<String> register(@Body LoginRequest req) {
        try {
            var token = authenticationService.register(req.username(), req.password());
            return HttpResponse.ok(token);
        } catch (AuthenticationException e) {
            return HttpResponse.badRequest();
        }
    }

    public record LoginRequest(String username, String password) {}
}
