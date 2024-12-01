package service.infrastructure.db;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import io.micronaut.http.client.annotation.Client;
import service.application.UserDatabase;
import service.domain.User;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Adapter for UserDatabase port. Uses a file-based db and the service account manager
 */
@Singleton
public class FileAndClientUserDB implements UserDatabase {

    private final File db;
    private final List<User> users;
    @Inject
    @Client("http://account-manager:8080/users")
    private HttpClient httpClient;

    @Inject
    public FileAndClientUserDB() {
        this(Config.databasePath);
    }

    public FileAndClientUserDB(String path) {
        URL url = this.getClass().getResource("/" + path);
        try {
            assert url != null;
            this.db = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.users = readAllUsers();
    }
    @Override
    public boolean contains(User user) {
        return users.contains(user) && accountManagerContains(user.username());
    }

    @Override
    public void addUser(User user) {
        if (users.stream().noneMatch(u -> u.username().equals(user.username()))) {
            users.add(user);
            accountManagerContains(user.username());
        }
    }

    @Override
    public List<User> getUsers() {
        return this.users;
    }

    // query the remote service
    private boolean accountManagerContains(String username) {
        try {
            HttpRequest<?> request = HttpRequest.GET("/" + username).header(HttpHeaders.AUTHORIZATION, Config.serviceToken);
            // send request and wait
            return httpClient.toBlocking().retrieve(request, Optional.class).isPresent();
        } catch (Exception e) {
            return false;
        }
    }
    // post to the remote service
    private void accountManagerAddUser(String username) {
        try {
            HttpRequest<?> request = HttpRequest.POST("/add", new UserAdd(username)).header(HttpHeaders.AUTHORIZATION, Config.serviceToken);
            // send request and wait
            httpClient.toBlocking().retrieve(request, String.class);
        } catch (Exception ignored) {}
    }

    private List<User> readAllUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(db))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    private void writeAllUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(db))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Introspected
    private record UserInfo(String username, String credits) {}
    @Introspected
    private record UserAdd(String username) {}
}