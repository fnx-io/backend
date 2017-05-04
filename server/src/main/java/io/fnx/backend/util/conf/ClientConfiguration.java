package io.fnx.backend.util.conf;

import com.google.appengine.api.utils.SystemProperty;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import io.fnx.backend.domain.FileCategory;
import io.fnx.backend.util.EnumerationRepository;
import io.fnx.backend.util.MessageAccessor;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

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
