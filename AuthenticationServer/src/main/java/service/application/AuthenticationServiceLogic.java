package service.application;

import jakarta.inject.Singleton;
import service.domain.AuthenticationException;
import service.domain.User;

/**
 * Adapter for Authentication service. Uses a database and a token generator
 */
@Singleton
public class AuthenticationServiceLogic implements AuthenticationService {

    private final TokenGenerator tokenGenerator;
    private final UserDatabase userDatabase;

    public AuthenticationServiceLogic(TokenGenerator tokenGenerator, UserDatabase userDatabase) {
        this.tokenGenerator = tokenGenerator;
        this.userDatabase = userDatabase;
    }

    @Override
    public String generateToken(String username, String password) throws AuthenticationException {
        return tokenGenerator.generate(new User(username, password));
    }

    @Override
    public String authenticate(String username, String password) throws AuthenticationException {
        User user = new User(username, password);
        if (!userDatabase.contains(user)) {
            throw new AuthenticationException("Invalid username or password");
        } else {
            return generateToken(username, password);
        }
    }

    @Override
    public String register(String username, String password) throws AuthenticationException {
        userDatabase.addUser(new User(username, password));
        return authenticate(username, password);
    }
}
