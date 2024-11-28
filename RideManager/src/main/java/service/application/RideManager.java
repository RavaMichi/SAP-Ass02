package service.application;

import service.domain.RideManagerException;

/** Inbound port
 * Service object. Represents the rental service
 */
public interface RideManager {
    void connectUserToBike(String userId, String bikeId) throws RideManagerException;
    void disconnectUserFromBike(String userId, String bikeId) throws RideManagerException;

    void addListener(RideManagerListener listener);
}
