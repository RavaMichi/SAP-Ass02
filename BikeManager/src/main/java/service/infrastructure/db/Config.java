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

    private final AuthChecker authChecker;
    private final FileBikeDatabase fileBikeDatabase;

    public Config(AuthChecker authChecker, FileBikeDatabase fileBikeDatabase) {
        this.authChecker = authChecker;
        this.fileBikeDatabase = fileBikeDatabase;
    }

    @Post("/set-db-path")
    public HttpResponse<String> setDbPath(@Header(HttpHeaders.AUTHORIZATION) String token, @Body String dbPath) {
        if (authChecker.hasAdminPermissions(token)) {
            databasePath = dbPath;
            fileBikeDatabase.init(databasePath);
            return HttpResponse.ok(databasePath);
        } else {
            return HttpResponse.unauthorized();
        }
    }
}
