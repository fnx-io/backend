package io.fnx.backend.social;

import io.fnx.backend.domain.UserEntity;

public class SocialUserInfo {

	String socialMediaId;
	UserEntity user;

	public String getSocialMediaId() {
		return socialMediaId;
	}

	public void setSocialMediaId(String socialMediaId) {
		this.socialMediaId = socialMediaId;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
}
