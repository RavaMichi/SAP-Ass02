package service.application;

import service.domain.*;
import java.util.Optional;

/** Inbound Port
 * Service object. Represents the ebike manager system.
 */
public interface BikeManager {
    Optional<EBike> getBike(String id);
    void addBike(String id, int battery, V2d position) throws BikeOperationException;
}
