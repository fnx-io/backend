package io.fnx.backend.util.conf;

import io.fnx.backend.util.EnumerationRepository;
import io.fnx.backend.util.MessageAccessor;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

/**
 *
 *  Use this class for sending configuration to Dart admin client. Enums, settings, flags, enabled modules etc.
 *  Anything you wish to manage from the server goes here.
 *
 */
public class ClientConfiguration {

	private final Map<String, String> messages;
	private final EnumerationRepository enumerations;

	@Inject
	public ClientConfiguration(EnumerationRepository enumerationRepository, MessageAccessor messageAccessor) throws IOException {
		this.enumerations = enumerationRepository;
		this.messages = messageAccessor.buildMessagesMap();
	}


	public Map<String, String> getMessages() {
		return messages;
	}

	public EnumerationRepository getEnumerations() {
		return enumerations;
	}

}
