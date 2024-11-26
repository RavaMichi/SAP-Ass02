package service.infrastructure;

import org.junit.jupiter.api.Test;
import service.application.BikeManager;
import service.application.BikeManagerLogic;
import service.infrastructure.db.FileBikeDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration testing
 */
public class BikeManagerTests {

    private final BikeManager bikeManager = new BikeManagerLogic(new FileBikeDatabase());

    @Test
    public void testListBikes() {
        var bikes = bikeManager.getAllBikes();
        assertNotNull(bikes);
    }
}
