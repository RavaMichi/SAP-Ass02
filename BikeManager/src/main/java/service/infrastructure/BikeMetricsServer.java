package service.infrastructure;

import io.micrometer.core.instrument.*;
import jakarta.inject.Singleton;
import service.application.BikeManager;
import service.application.BikeManagerListener;
import service.domain.EBike;

@Singleton
public class BikeMetricsServer implements BikeManagerListener {

    private final Counter totalBikes;
    private double avgBatteryLevel;

    public BikeMetricsServer(BikeManager bikeManager, MeterRegistry meterRegistry) {
        bikeManager.addListener(this);

        // track the number of bikes
        totalBikes = Counter.builder("number_of_bikes")
                .description("The total number of bikes registered in the service")
                .register(meterRegistry);

        // track the average battery level
        Gauge.builder("average_battery", this, BikeMetricsServer::getAvgBatteryLevel)
                .description("The average level of battery of all bikes")
                .register(meterRegistry);

        // init all counters
        totalBikes.increment(bikeManager.getAllBikes().size());
        avgBatteryLevel = bikeManager.getAllBikes().stream()
                .mapToInt(EBike::getBatteryLevel)
                .average()
                .orElse(0.0);

        System.out.println("Prometheus ready");
    }

    private double getAvgBatteryLevel() {
        return avgBatteryLevel;
    }

    @Override
    public void onBikeAdd(EBike newBike) {
        totalBikes.increment();
        avgBatteryLevel = recalculateAverage(newBike.getBatteryLevel(), true);
    }

    @Override
    public void onBikeUpdate(EBike previousBike, EBike updatedBike) {
        avgBatteryLevel = recalculateAverage(updatedBike.getBatteryLevel() - previousBike.getBatteryLevel(), false);
    }

    private double recalculateAverage(double delta, boolean isAddition) {
        double totalBikesCount = totalBikes.count();
        double currentTotalBattery = avgBatteryLevel * totalBikesCount;

        return (currentTotalBattery + delta) / totalBikesCount;
    }

}
