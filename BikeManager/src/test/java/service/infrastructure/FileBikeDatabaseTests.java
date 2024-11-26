package service.infrastructure;

import org.junit.jupiter.api.Test;
import service.application.BikeDatabase;
import service.domain.EBike;
import service.domain.V2d;
import service.infrastructure.db.FileBikeDatabase;

public class FileBikeDatabaseTests {

    private final String filePath = ClassLoader.getSystemClassLoader().getResource("db/.bikes").getPath();
    @Test
    public void testDatabaseCreation() {
        BikeDatabase db = new FileBikeDatabase(filePath);
    }
    @Test
    public void testDatabaseInsertion() {
        BikeDatabase db = new FileBikeDatabase(filePath);
        var bike = new EBike("b1", 2, new V2d(0,0));
        db.add(bike);
    }
    @Test
    public void testDatabaseRetrieval() {
        BikeDatabase db = new FileBikeDatabase(filePath);
        var bikes = db.getAll();
        System.out.println(bikes);
    }
}
