package service.application;

import service.domain.*;

import java.util.List;
import java.util.Optional;

/** Outbound Port
 * Repository object. Allows to persist and query bikes' states.
 */
public interface BikeDatabase {
    Optional<EBike> get(String id);
    List<EBike> getAll();
    void add(EBike bike);
    void setBattery(EBike bike, int battery);
    void setPosition(EBike bike, V2d position);
}
