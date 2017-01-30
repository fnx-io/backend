package com.backend.util.conf;

import com.google.appengine.api.utils.SystemProperty;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

import static java.lang.String.format;

/**
 * Enables convenient access to configuration (System) properties,
 * which can be easily set in <code>appengine-web.xml</code>
 * <code>system-properties</code> section.
 * <br>
 * Example:
 * <pre>
 *   &lt;?xml version="1.0" encoding="utf-8"?&gt;
 *   &lt;appengine-web-app xmlns="http://appengine.google.com/ns/1.0"&gt;
 *     .
 *     .
 *     .
 *     &lt;system-properties&gt;
 *       &lt;property name="app.counter" value="123"/&gt;
 *     &lt;/system-properties&gt;
 *   &lt;/appengine-web-app&gt;
 * </pre>
 */
public class AppConfiguration {


    /**
     * Returns the value of given key as an <code>Integer</code>
     * @param key the configuration key
     * @return the value for configuration key as integer
     * @throws InvalidConfigurationKeyException when the value is null or not an integer
     */
    public int getIntegerProperty(String key) {
        final String val = getProperty(key);
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationKeyException(key, format("[%s] could not be converted to an int", val));
        }
    }

    /**
     * Returns the value of given key as a <code>double</code>
     * @param key the configuration key
     * @return the value for configuration key as integer
     * @throws InvalidConfigurationKeyException when the value is null or not a double value
     */
    public double getDoubleProperty(String key) {
        final String val = getProperty(key);
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationKeyException(key, format("[%s] could not be converted to an double", val));
        }
    }

    /**
     * Returns the value of given key as a <code>List</code>
     * @param key the configuration key
     * @return the value for configuration key splitted on <code>','</code>. Every item is also trimmed.
     * @throws InvalidConfigurationKeyException when the value is not present
     */
    public List<String> getListProperty(String key) {
        final String val = getProperty(key);
        if (val.isEmpty())  return Lists.newLinkedList();
        Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split(val);
        return Lists.newArrayList(split);
    }

    /**
     * Returns the value of given key as a <code>boolean</code>
     * @param key the configuration key
     * @return <code>true if the configuration value is a string "<em>true</em>", every other value supplied value
     * will resolve to <code>false</code>
     * @throws InvalidConfigurationKeyException when the value is not present
     */
    public boolean getBooleanProperty(String key) {
        return "true".equals(getProperty(key));
    }

    /**
     * Returns the value of given key as a <code>String</code>
     * @param key the configuration key
     * @return the value for configuration key
     * @throws InvalidConfigurationKeyException when the value is not present
     */
    public String getProperty(String key) {
        final String value = System.getProperty(key);
        if (value == null) throw new InvalidConfigurationKeyException(key, format("Missing configuration key: %s", key));
        return value;
    }

    /**
     * @return <code>true</code> if the appengine runtime is ran on appengine's infrastructure,
     * <code>false</code> if it is ran inside the dev environment
     */
    public boolean isProduction() {
        return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
    }

    /**
     * @return <code>false</code> if the appengine runtime is ran on appengine's infrastructure,
     * <code>true</code> if it is ran inside the dev environment
     */
    public boolean isDevelopment() {
        return !isProduction();
    }
}
