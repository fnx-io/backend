package io.fnx.backend.social.web;

import com.google.inject.Inject;
import io.fnx.backend.domain.dto.login.LoginResult;
import io.fnx.backend.service.UserService;
import io.fnx.backend.social.SocialPlugin;
import io.fnx.backend.social.SocialUserInfo;
import io.fnx.backend.util.conf.BackendConfiguration;
import io.fnx.backend.web.BaseController;
import io.fnx.backend.web.UserController;
import org.mint42.annotations.OnAction;
import org.mint42.resolution.RedirectResolution;
import org.mint42.resolution.Resolution;
import org.mint42.util.Url;

public abstract class BaseSocialController extends BaseController {

	@Inject
	private BackendConfiguration backendConfiguration;

	@Inject
	private UserService userService;

	protected abstract SocialPlugin getPlugin();

	protected abstract String getFlavour();
	
	/**
	 * Redirect to oAuth web site.
	 * @return
	 */
	@OnAction("doRedirect")
	public Resolution doRedirect() {
		Url ret = getPlugin().authenticateUrl(getReturnUrl(), mintContext.getRequest());
		return new RedirectResolution(ret.toString());
	}

	@OnAction("doReturn")
	public Resolution doReturn() {
		try {
			String token = getPlugin().getAccessToken(getReturnUrl(), mintContext.getRequest());
			SocialUserInfo user = getPlugin().getUserInfo(token);

			LoginResult result = userService.loginOnCreate(user.getUser(), user.getSocialMediaId(), getFlavour());
			UserController.loginImpl(mintContext, callContext, result.getToken(), result.getUser());
			return new RedirectResolution("/");
			
		} catch (Exception e) {
			log.error("Cannot log-in via social media: "+e, e);
			return redirectWithError("/", "error.cannotLoginViaSocial");
		}
	}
	
	protected String getReturnUrl() {
		String root = backendConfiguration.getProperty("root");
		return root+"/social/"+getFlavour()+"/return";
	}

}
