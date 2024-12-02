package service.infrastructure.auth;

import jakarta.inject.Singleton;

@Singleton
public class SimpleUserAuthChecker implements AuthChecker {
    @Override
    public boolean hasUserPermissions(String token) {
        return token.equals("AUTHORIZED") || token.equals("ADMIN");
    }

    @Override
    public boolean hasAdminPermissions(String token) {
        return token.equals("ADMIN");
    }
}
