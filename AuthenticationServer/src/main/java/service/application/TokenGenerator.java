package service.application;

/** Outbound port
 * Service object, creates token for the logging users
 */
public interface TokenGenerator {
    String generate(String username);
}
