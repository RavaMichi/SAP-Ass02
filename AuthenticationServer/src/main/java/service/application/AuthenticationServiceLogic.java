package service.application;

import jakarta.inject.Singleton;
import service.domain.AuthenticationException;

@Singleton
public class AuthenticationServiceLogic implements AuthenticationService {

    private final TokenGenerator tokenGenerator;
    private final UserDatabase userDatabase;

    public AuthenticationServiceLogic(TokenGenerator tokenGenerator, UserDatabase userDatabase) {
        this.tokenGenerator = tokenGenerator;
        this.userDatabase = userDatabase;
    }

    @Override
    public String authenticate(String username, String password) throws AuthenticationException {
        return "";
    }

    @Override
    public String addUser(String username, String password) throws AuthenticationException {
        return "";
    }
}
