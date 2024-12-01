package service.application;

import jakarta.inject.Singleton;
import service.domain.User;

/**
 * Easy implementation for token gen
 */
@Singleton
public class SimpleTokenGenerator implements TokenGenerator {
    @Override
    public String generate(User user) {
        return "AUTHORIZED";
    }
}