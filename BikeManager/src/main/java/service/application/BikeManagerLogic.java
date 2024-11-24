package service.application;

import service.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of both BikeManager and BikeEndpoint
 */
public class BikeManagerLogic implements BikeManager, BikeEndpoint {

    private BikeDatabase bikeDatabase;
    private List<BikeManagerListener> listeners = new ArrayList<>();

    public BikeManagerLogic(BikeDatabase bikeDatabase) {
        this.bikeDatabase = bikeDatabase;
    }

    @Override
    public Optional<EBike> getBike(String id) {
        return bikeDatabase.get(id);
    }

    @Override
    public List<EBike> getAllBikes() {
        return bikeDatabase.getAll();
    }

    @Override
    public void addBike(String id, int battery, V2d position) throws BikeOperationException {
        var bike = bikeDatabase.get(id);
        if (bike.isPresent()) {
            throw new BikeOperationException("EBike " + id + " already exists");
        } else {
            var newBike = new EBike(id, battery, position);
            bikeDatabase.add(newBike);
            listeners.forEach(l -> l.onBikeAdd(newBike));
        }
    }

    @Override
    public void updateBike(String id, int battery, V2d position) throws BikeOperationException {
        var bike = bikeDatabase.get(id);
        if (bike.isPresent()) {
            bikeDatabase.setBattery(bike.get(), battery);
            bikeDatabase.setPosition(bike.get(), position);
            listeners.forEach(l -> l.onBikeUpdate(bike.get(), bikeDatabase.get(id).get()));
        } else {
            throw new BikeOperationException("EBike " + id + " does not exist");
        }
    }

    @Override
    public void addListener(BikeManagerListener listener) {
        this.listeners.add(listener);
    }
}
