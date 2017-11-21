package io.fnx.backend.social.gplus.impl;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.service.UserService;
import io.fnx.backend.social.BaseSocialPlugin;
import io.fnx.backend.social.SocialUserInfo;
import io.fnx.backend.social.exception.PluginException;
import io.fnx.backend.social.gplus.GooglePlusSocialPlugin;
import io.fnx.backend.util.conf.BackendConfiguration;
import org.mint42.util.Url;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Plugin pro prihlaseni do socialni site Google+
 *
 * @author Pecina Ondrej <pecina.ondrej@gmail.com>
 */
@Singleton
public class GooglePlusSocialPluginImpl extends BaseSocialPlugin<GooglePermission> implements GooglePlusSocialPlugin {

	private static final String LOGIN_URL = "https://accounts.google.com/o/oauth2/auth";
	private static final String ACCESS_TOKEN = "https://accounts.google.com/o/oauth2/token";
	private static final String DETAIL_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

	@Inject
	private UserService userService;

	@Inject
	private BackendConfiguration backendConfiguration;

	@Inject
	public GooglePlusSocialPluginImpl(Gson gson) {
		super(gson);

		// base permission required to login
		addPermission(GooglePermission.BASIC_INFO);
		addPermission(GooglePermission.ME);
		addPermission(GooglePermission.EMAIL);
	}

	@Override
	protected String getAppId() {
		String appId = backendConfiguration.getProperty("social.google.id");
		Preconditions.checkNotNull(appId, "Missing configuration property social.google.id");
		return appId;
	}

	@Override
	protected String getAppSecret() {
		String secretId = backendConfiguration.getProperty("social.google.secret");
		Preconditions.checkNotNull(secretId, "Missing configuration property social.google.secret");
		return secretId;
	}

	@Override
	public Url authenticateUrl(String redirectUrl, HttpServletRequest request) {
		Preconditions.checkNotNull(getPermissions(), "Missing permissions for login");

		Url authUrl = new Url(LOGIN_URL);
		authUrl.add("client_id", getAppId());
		authUrl.add("redirect_uri", redirectUrl);
		authUrl.add("scope", getPermissions().replace(",", " "));
		authUrl.add("access_type", "online");
		authUrl.add("response_type", "code");

		return authUrl;
	}

	@Override
	public SocialUserInfo getUserInfo(String accessToken) {
		Url userInfoUrl = new Url(DETAIL_URL);
		userInfoUrl.add("access_token", accessToken);

		JsonElement e = gson.fromJson(fetchUrl(userInfoUrl), JsonElement.class);

		log.info("Received user data: "+e);

		SocialUserInfo info = new SocialUserInfo();
		UserEntity account = new UserEntity();
		JsonObject o = e.getAsJsonObject();
		info.setSocialMediaId(o.get("id").getAsString());
		account.setFirstName(o.get("given_name").getAsString());
		account.setLastName(o.get("family_name").getAsString());
		/*
		if (o.has("link")) {
		}
		*/
		account.setAvatarUrl(o.get("picture").getAsString());
		account.setEmail(o.get("email").getAsString());
		//account.setAccessToken(accessToken);
		//account.setType(Account.Type.G_PLUS);

		info.setUser(account);

		return info;
	}

	@Override
	public String getAccessToken(String redirectUrl, HttpServletRequest request) {
        if (request.getParameter("code") == null) {
            // missing code, something went wrong
            throw new PluginException("missing 'code' in G response");
        }

		Map<String, String> paramsMap = new HashMap<>();

		paramsMap.put("client_id", getAppId());
		paramsMap.put("redirect_uri", redirectUrl);
		paramsMap.put("client_secret", getAppSecret());
		paramsMap.put("grant_type", "authorization_code");
		paramsMap.put("code", request.getParameter("code"));

		String json = postUrl(new Url(ACCESS_TOKEN), paramsMap);

		TokenResponse tokenResponse = gson.fromJson(json, TokenResponse.class);

		return tokenResponse.getAccess_token();
	}

	/**
	 * Google plus acessToken respnse
	 * TODO: Makes more with this information
	 */
	private static class TokenResponse {
		private String access_token;
		private String token_type;
		private String id_token;
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

		private String getId_token() {
			return id_token;
		}

		private void setId_token(String id_token) {
			this.id_token = id_token;
		}

		private long getExpires_in() {
			return expires_in;
		}

		private void setExpires_in(long expires_in) {
			this.expires_in = expires_in;
		}
	}
}

