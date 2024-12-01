package service.application;

import service.domain.AuthenticationException;

public class AuthenticationServiceLogic implements AuthenticationService {

    private final TokenGenerator tokenGenerator;
    private final UserDatabase userDatabase;

    @Override
    public String authenticate(String username, String password) throws AuthenticationException {
        return "";
    }

    @Override
    public String addUser(String username, String password) throws AuthenticationException {
        return "";
    }
}
