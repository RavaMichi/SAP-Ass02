package service.infrastructure;

public interface AuthChecker {
    boolean isAuthorized(String token);
}
