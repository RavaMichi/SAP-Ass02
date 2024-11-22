package service.application;

import service.domain.*;

import java.util.List;

/** Outbound Port
 * Repository object. Allows to persist and query bikes' states.
 */
public interface BikeDatabase {
    EBike get(String id);
    List<EBike> getAll();
    void add(EBike bike);
    void setBattery(EBike bike, int battery);
    void setPosition(EBike bike, V2d position);
}
