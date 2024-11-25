package service.infrastructure;

import io.micronaut.core.annotation.Introspected;

/**
 * Response to a bike update request
 *
 * @param message
 * @param error
 */
@Introspected
public record BMResponse(
        String message,
        boolean error
) {
}
