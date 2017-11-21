package io.fnx.backend.social.facebook.impl;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.social.BaseSocialPlugin;
import io.fnx.backend.social.SocialUserInfo;
import io.fnx.backend.social.exception.PluginException;
import io.fnx.backend.social.facebook.FacebookSocialPlugin;
import org.mint42.util.Url;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pecina Ondrej <pecina.ondrej@gmail.com>
 */
@Singleton
public class FacebookSocialPluginImpl extends BaseSocialPlugin<FacebookPermission> implements FacebookSocialPlugin {

	private static final Url LOGIN_URL = new Url("https://www.facebook.com/dialog/oauth");
	private static final Url DETAIL_URL = new Url("https://graph.facebook.com/me?fields=first_name,last_name,email");
	private static final Url ACCESS_TOKEN = new Url("https://graph.facebook.com/oauth/access_token");
	private static final Url FRIEND_LIST = new Url("https://graph.facebook.com/me/friends");
	private static final Url PROFILE_PHOTO = new Url("https://graph.facebook.com/{PROFILE_ID}/picture?type=large");

	@Inject
	public FacebookSocialPluginImpl(Gson gson) {
		super(gson);
		addPermission(FacebookPermission.EMAIL);
	}

	@Override
	protected String getAppId() {
		String secretId = backendConfiguration.getProperty("social.facebook.id");
		return secretId;
	}

	@Override
	protected String getAppSecret() {
		String secretId = backendConfiguration.getProperty("social.facebook.secret");
		Preconditions.checkNotNull(secretId, "Missing configuration property fbSecretId");
		return secretId;
	}

	@Override
	public Url authenticateUrl(String redirectUrl, HttpServletRequest request) {
		Preconditions.checkNotNull(getPermissions(), "Missing permissions for login");

		Url authUrl = new Url(LOGIN_URL);
		authUrl.add("client_id", getAppId());
		authUrl.add("redirect_uri", redirectUrl);
		authUrl.add("scope", getPermissions());
		authUrl.add("state", getStamp());
		return authUrl;
	}

	@Override
	public String getAccessToken(String redirectUrl, HttpServletRequest request) {
        if (request.getParameter("code") == null) {
            // missing code, something went wrong
            throw new PluginException("missing 'code' in F response");
        }
		Url accessTokenUrl = new Url(ACCESS_TOKEN);
		accessTokenUrl.add("code", request.getParameter("code"));
		accessTokenUrl.add("client_id", getAppId());
		accessTokenUrl.add("client_secret", getAppSecret());
		accessTokenUrl.add("redirect_uri", redirectUrl);

		if (!getStamp().equals(request.getParameter("state"))) {
			throw new IllegalStateException("Bad salted state");
		}

		String response = fetchUrl(accessTokenUrl);
		return refreshAccessToken(parseAccessToken(response));
	}
	
	@Override
	public String refreshAccessToken(String accessToken) {
		Url accessTokenUrl = new Url(ACCESS_TOKEN);
		accessTokenUrl.add("client_id", getAppId());
		accessTokenUrl.add("client_secret", getAppSecret());
		accessTokenUrl.add("grant_type", "fb_exchange_token");
		accessTokenUrl.add("fb_exchange_token", accessToken);

		String response = fetchUrl(accessTokenUrl);

		return parseAccessToken(response);
	}

	@Override
	public SocialUserInfo getUserInfo(String accessToken) {
		Url detailUrl = new Url(DETAIL_URL);
		detailUrl.add("access_token", accessToken);

		JsonElement e = gson.fromJson(fetchUrl(detailUrl), JsonElement.class);
		log.info("Received user data: "+e);

		SocialUserInfo info = new SocialUserInfo();
		UserEntity account = new UserEntity();
		JsonObject o = e.getAsJsonObject();
		info.setSocialMediaId(o.get("id").getAsString());
		account.setFirstName(o.get("first_name").getAsString());
		account.setLastName(o.get("last_name").getAsString());
		//account.setAvatarUrl(o.get("link").getAsString());
		account.setAvatarUrl(getProfilePhotoUrl(info.getSocialMediaId()));
		account.setEmail(o.get("email").getAsString());
		//account.setAccessToken(accessToken);
		//account.setLocale(o.get("locale").getAsString());
		//account.setGender(o.get("gender").getAsString());
		//account.setAccessToken(accessToken);
		//account.setType(Account.Type.FACEBOOK);

		info.setUser(account);
		return info;
	}

	@Override
	public List<String> getFriends(String accessToken) {
		// ziskam seznam pratel, kteri jsou zapojeni do apky
		Url friendUrl = new Url(FRIEND_LIST);
		friendUrl.add("access_token", accessToken);
		String response = fetchUrl(friendUrl);

		// prevedu na pozuitelny objekt
		FriendsList friendsList = gson.fromJson(response, FriendsList.class);

		// seznam pratel pouze jejich id
		List<String> retVal = new ArrayList<>();
		for (FriendsList.Friend friend : friendsList.getData()) {
			retVal.add(friend.getId());
		}
		return retVal;
	}

	private String getProfilePhotoUrl(String accountId) {
		return "https://graph.facebook.com/"+accountId+"/picture?width=500&height=500";
	}

	private String parseAccessToken(String response) {

		/*if (response == null || response.indexOf("token=") <= 0) {
			throw new PluginException("Invalid response from facebook, access to accesToken disallowed");
		}

		response = response.replace("access_token=", "");
		return response.substring(0, response.indexOf("&"));*/
		if (response != null) {
			TokenResponse tokenResponse = gson.fromJson(response, TokenResponse.class);
			return tokenResponse.getAccess_token();
		}
		return null;
	}

	/**
	 * Seznam pratel ze socialni site facebook
	 */
	public static class FriendsList {
		private List<Friend> data;

		private Paging paging;

		public List<Friend> getData() {
			return data;
		}

		public void setData(List<Friend> data) {
			this.data = data;
		}

		public Paging getPaging() {
			return paging;
		}

		public void setPaging(Paging paging) {
			this.paging = paging;
		}

		public static class Paging {
			private String next;

			public void setNext(String next) {
				this.next = next;
			}

			public String getNext() {
				return next;
			}
		}

		public static class Friend {
			private String name;

			private String id;

			public String getName() {
				return name;
			}

			public String getId() {
				return id;
			}
		}
	}

	private static class TokenResponse {
		private String access_token;
		private String token_type;
		private long expires_in;

		private String getAccess_token() {
			return access_token;
		}

		private void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

		private String getToken_type() {
			return token_type;
		}

		private void setToken_type(String token_type) {
			this.token_type = token_type;
		}

		private long getExpires_in() {
			return expires_in;
		}

		private void setExpires_in(long expires_in) {
			this.expires_in = expires_in;
		}
	}

}
