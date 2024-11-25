package service.infrastructure;

import service.application.BikeManagerListener;
import service.domain.EBike;

public class BikeMetricsServer implements BikeManagerListener {

    @Override
    public void onBikeAdd(EBike newBike) {

    }

    @Override
    public void onBikeUpdate(EBike previousBike, EBike updatedBike) {

    }
}
