package service.application;

import service.domain.EBike;
import service.domain.V2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Mockup for the bike database
 */
public class MockBikeDatabase implements BikeDatabase {
    private List<EBike> bikes = new ArrayList<>();
    @Override
    public Optional<EBike> get(String id) {
        return bikes.stream().filter(b -> Objects.equals(b.getID(), id)).findFirst();
    }

    @Override
    public List<EBike> getAll() {
        return List.copyOf(bikes);
    }

    @Override
    public void add(EBike bike) {
        this.bikes.add(bike);
    }

    @Override
    public void setBatteryAndPosition(EBike bike, int battery, V2d position) {
        bikes.stream().filter(b -> Objects.equals(b.getID(), bike.getID())).findFirst().ifPresent(b -> {
            b.updateBatteryLevel(battery);
            b.updatePosition(position);
        });
    }
}
