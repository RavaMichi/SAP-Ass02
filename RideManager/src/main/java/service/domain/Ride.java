package service.domain;

/**
 * Entity object. Represents a rental session of a user.
 * There cannot be two rides with either the same user or bike.
 */
public record Ride(String userId, String bikeId) {
}
