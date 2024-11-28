package service.infrastructure;

import org.junit.jupiter.api.Test;
import service.application.BikeDatabase;
import service.domain.EBike;
import service.domain.V2d;
import service.infrastructure.db.FileBikeDatabase;

/**
 * Unit testing
 */
public class FileBikeDatabaseTests {

    private final String filePath = "db/.bikes";
    @Test
    public void testDatabaseCreation() {
        System.out.println(filePath);
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
