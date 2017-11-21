package io.fnx.backend.social.gplus.impl;

import io.fnx.backend.social.SocialPermission;

/**
 * Seznam prav pro G+
 *
 * @author Pecina Ondrej <pecina.ondrej@gmail.com>
 */
public enum GooglePermission implements SocialPermission {
	BASIC_INFO("profile"),
	PROFILE("https://www.googleapis.com/auth/plus.login"),
	READ_EMAIL("https://www.googleapis.com/auth/plus.profile.emails.read"),
	OPENID("openid"),
	ME("https://www.googleapis.com/auth/plus.me"),
	EMAIL("email");

	private String scopeName;

	private GooglePermission(String scopeName) {
		this.scopeName = scopeName;
	}

	public String getScopeName() {
		return scopeName;
	}
}