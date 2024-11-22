package service.application;

import service.domain.*;

import java.util.List;
import java.util.Optional;

public class BikeManagerLogic implements BikeManager, BikeEndpoint {

    private BikeDatabase bikeDatabase;
    @Override
    public Optional<EBike> getBike(String id) throws BikeOperationException {
        return Optional.empty();
    }

    @Override
    public List<EBike> getAllBikes() throws BikeOperationException {
        return null;
    }

    @Override
    public void addBike(String id, int battery, V2d position) throws BikeOperationException {

    }

    @Override
    public void updateBike(String id, int battery, V2d position) {

    }

    @Override
    public void addListener(BikeManagerListener listener) {

    }
}
