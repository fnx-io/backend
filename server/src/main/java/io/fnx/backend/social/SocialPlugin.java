package io.fnx.backend.social;

import io.fnx.backend.domain.UserEntity;
import org.mint42.util.Url;

import javax.servlet.http.HttpServletRequest;

/**
 * Plugin pro prihlasovani do socialni site
 *
 * @author Pecina Ondrej <pecina.ondrej@gmail.com>
 */
public interface SocialPlugin<PERMISSION extends SocialPermission> {

	/**
	 * Vygeneruje autorizacni url
	 *
	 * @param redirectUrl
	 * @param request
	 * @return - URL adresa k prihlaseni do socialni site
	 */
	Url authenticateUrl(String redirectUrl, HttpServletRequest request);

	/**
	 * Vraci informace o prihlasenem uzivateli v podobe Account.
	 *
	 * @param accessToken
	 * @return
	 */
	SocialUserInfo getUserInfo(String accessToken);

	/**
	 * Vrati acessToken socialni site
	 *
	 * @param redirectUrl
	 * @param request
	 * @return
	 */
	String getAccessToken(String redirectUrl, HttpServletRequest request);

	/**
	 * Prida pravo pluginu
	 *
	 * @param permission
	 */
	void addPermission(PERMISSION permission);

}
