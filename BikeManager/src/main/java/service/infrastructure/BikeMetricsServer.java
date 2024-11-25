package service.infrastructure;

import jakarta.inject.Singleton;
import service.application.BikeManager;
import service.application.BikeManagerListener;
import service.domain.EBike;

@Singleton
public class BikeMetricsServer implements BikeManagerListener {

    public BikeMetricsServer(BikeManager bikeManager) {
        bikeManager.addListener(this);
    }

    @Override
    public void onBikeAdd(EBike newBike) {

    }

    @Override
    public void onBikeUpdate(EBike previousBike, EBike updatedBike) {

    }
}
