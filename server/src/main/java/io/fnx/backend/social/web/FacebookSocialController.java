package io.fnx.backend.social.web;

import com.google.inject.Inject;
import io.fnx.backend.social.SocialPlugin;
import io.fnx.backend.social.facebook.impl.FacebookSocialPluginImpl;

public class FacebookSocialController extends BaseSocialController {

	@Inject
	private FacebookSocialPluginImpl plugin;

	protected SocialPlugin getPlugin() {
		return plugin;
	}

	protected String getFlavour() {
		return "facebook";
	}
	
}
