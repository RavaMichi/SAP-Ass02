package service.infrastructure.db;

import jakarta.inject.Singleton;

@Singleton
public class Config {
    public final static String databasePath = "db/.pwd";
    public final static CharSequence serviceToken = "AUTHORIZED";
}
