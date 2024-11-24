package service.infrastructure;

import service.application.BikeDatabase;
import service.domain.EBike;
import service.domain.V2d;

import java.io.*;
import java.util.*;

/**
 * File-based adapter of Bike Database port
 */
public class FileBikeDatabase implements BikeDatabase {

    private final File db;
    private final List<EBike> bikes;

    public FileBikeDatabase(String path) {
        this.db = new File(path);
        this.bikes = readAllBikes();
    }
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
        writeAllBikes();
    }

    @Override
    public void setBatteryAndPosition(EBike bike, int battery, V2d position) {
        bikes.stream().filter(b -> Objects.equals(b.getID(), bike.getID())).findFirst().ifPresent(b -> {
            b.updateBatteryLevel(battery);
            b.updatePosition(position);
        });
        writeAllBikes();
    }

    private List<EBike> readAllBikes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(db))) {
            return (List<EBike>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    private void writeAllBikes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(db))) {
            oos.writeObject(bikes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
