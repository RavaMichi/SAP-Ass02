package service.application;

import jakarta.inject.Singleton;
import service.domain.AccountOperationException;
import service.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of AccountManager
 */
@Singleton
public class AccountManagerLogic implements AccountManager{

    private final AccountDatabase accountDatabase;
    private final List<AccountManagerListener> listeners = new ArrayList<>();

    public AccountManagerLogic(AccountDatabase accountDatabase) {
        this.accountDatabase = accountDatabase;
    }

    @Override
    public Optional<User> getUser(String username) {
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public void addUser(String username) throws AccountOperationException {

    }

    @Override
    public void addCreditToUser(String username, int amount) throws AccountOperationException {

    }

    @Override
    public void removeCreditFromUser(String username, int amount) throws AccountOperationException {

    }

    @Override
    public void addListener(AccountManagerListener listener) {
        this.listeners.add(listener);
    }
}
