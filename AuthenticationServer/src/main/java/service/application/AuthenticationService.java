package service.application;

import service.domain.AuthenticationException;

/** Inbound port
 * Service object, representing the authenticator
 */
public interface AuthenticationService {
    String authenticate(String username, String password) throws AuthenticationException;
    String addUser(String username, String password) throws AuthenticationException;
}
