package io.fnx.backend.social.web;

import com.google.inject.Inject;
import io.fnx.backend.social.SocialPlugin;
import io.fnx.backend.social.gplus.impl.GooglePlusSocialPluginImpl;

public class GoogleSocialController extends BaseSocialController {

	@Inject
	private GooglePlusSocialPluginImpl plugin;

	protected SocialPlugin getPlugin() {
		return plugin;
	}

	protected String getFlavour() {
		return "google";
	}
	
}
