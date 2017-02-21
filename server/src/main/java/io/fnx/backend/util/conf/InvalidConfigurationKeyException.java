package io.fnx.backend.util.conf;

public class InvalidConfigurationKeyException extends IllegalStateException {

    public final String key;

    public InvalidConfigurationKeyException(String key, String message) {
        super(message);
        this.key = key;
    }

    @Override
    public String toString() {
        return "InvalidConfigurationKeyException: error while accessing configuration key " +
                key + ": " + getMessage();

    }
}
