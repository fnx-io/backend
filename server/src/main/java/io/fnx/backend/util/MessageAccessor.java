package io.fnx.backend.util;

import com.google.inject.Singleton;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Zkratka k ResouceBundle messages.
 *
 * @author Tomas Zverina, zverina@m-atelier.cz
 */
@Singleton
public class MessageAccessor {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.MESSAGES_RESOURCE_BUNDLE_NAME, Constants.LOCALE);

    /**
     * Returns our texts bundle.
     *
     * @return
     */
    public ResourceBundle getMessages() {
        return resourceBundle;
    }

    public String getMessage(String key) {
        return resourceBundle.getString(key);
    }

    public String getMessage(String key, Object... params) {
        return MessageFormat.format(resourceBundle.getString(key), params);
    }

	/**
	 * Pouziva se k odeslani hlasek na Dart klienta.
	 * 
	 * @return
	 */
	public Map<String, String> buildMessagesMap() {
		Map<String, String> result = new HashMap<>();
		Enumeration<String> keys = resourceBundle.getKeys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			result.put(key, resourceBundle.getString(key));
		}
		return result;
	}
	
}
