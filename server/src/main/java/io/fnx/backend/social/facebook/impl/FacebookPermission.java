package io.fnx.backend.social.facebook.impl;

import io.fnx.backend.social.SocialPermission;

/**
 * Seznam prav pro facebook
 *
 * @author Pecina Ondrej <pecina.ondrej@gmail.com>
 */
@SuppressWarnings("UnusedDeclaration")
public enum FacebookPermission implements SocialPermission {
	PUBLIC_PROFILE("public_profile"),
	EMAIL("email"),
	USER_ABOUT_ME("user_about_me"),
	USER_ACTIVITIES("user_activities"),
	USER_BIRTHDAY("user_birthday"),
	USER_CHECKINS("user_checkins"),
	USER_EDUCATION_HISTORY("user_education_history"),
	USER_EVENTS("user_events"),
	USER_GROUPS("user_groups"),
	USER_HOMETOWN("user_hometown"),
	USER_INTERESTS("user_interests"),
	USER_LIKES("user_likes"),
	USER_LOCATION("user_location"),
	USER_NOTES("user_notes"),
	USER_PHOTOS("user_photos"),
	USER_FRIENDS("user_friends"),
	USER_QUESTIONS("user_questions"),
	USER_RELATIONSHIPS("user_relationships"),
	USER_RELATIONSHIP_DETAILS("user_relationship_details"),
	USER_RELIGION_POLITICS("user_religion_politics"),
	USER_STATUS("user_status"),
	USER_SUBSCRIPTIONS("user_subscriptions"),
	USER_VIDEOS("user_videos"),
	USER_WEBSITE("user_website"),
	USER_WORK_HISTORY("user_work_history"),
	READ_FRIENDLISTS("read_friendlists"),
	READ_INSIGHTS("read_insights"),
	READ_MAILBOX("read_mailbox"),
	READ_REQUESTS("read_requests"),
	READ_STREAM("read_stream"),
	XMPP_LOGIN("xmpp_login"),
	USER_ONLINE_PRESENCE("user_online_presence"),
	FRIENDS_ONLINE_PRESENCE("friends_online_presence");

	private String scopeName;

	FacebookPermission(String scopeName) {
		this.scopeName = scopeName;
	}

	public String getScopeName() {
		return scopeName;
	}
}