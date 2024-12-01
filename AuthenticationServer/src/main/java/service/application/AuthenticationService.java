package service.application;

import service.domain.AuthenticationException;

/** Inbound port
 * Service object, representing the authenticator
 */
public interface AuthenticationService {
    String authenticate(String username, String password) throws AuthenticationException;
    String register(String username, String password) throws AuthenticationException;
}
