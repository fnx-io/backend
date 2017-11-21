package io.fnx.backend.social.facebook;

import io.fnx.backend.social.SocialPlugin;
import io.fnx.backend.social.facebook.impl.FacebookPermission;

import java.util.List;

/**
 * @author Pecina Ondrej <pecina.ondrej@gmail.com>
 */
public interface FacebookSocialPlugin extends SocialPlugin<FacebookPermission> {

	/**
	 * Get new long life access token to facebook
	 *
	 * @param accessToken - old access token
	 * @return - new access token
	 */
	String refreshAccessToken(String accessToken);

	/**
	 * Vrati seznam pratel z Facebooku, kteri jsou jiz pripojeni pres Facebook do aplikace
	 *
	 * @param accessToken   - platny pristupovy token
	 * @return              - seznam Facebook ID uzivatelu v aplikaci
	 */
	List<String> getFriends(String accessToken);

}
