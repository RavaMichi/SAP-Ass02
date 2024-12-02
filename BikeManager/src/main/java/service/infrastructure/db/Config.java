package service.infrastructure.db;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Singleton;
import service.infrastructure.auth.AuthChecker;

@Controller("/config")
public class Config {
    public static String databasePath = "db/.bikes";

    private AuthChecker authChecker;
    private FileBikeDatabase fileBikeDatabase;

    @Post("/set-db-path")
    public HttpResponse<String> setDbPath(@Header(HttpHeaders.AUTHORIZATION) String token, @Body String dbPath) {
        if (authChecker.hasAdminPermissions(token)) {
            databasePath = dbPath;
            return HttpResponse.ok(databasePath);
        } else {
            return HttpResponse.unauthorized();
        }
    }
}
