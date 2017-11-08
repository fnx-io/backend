package io.fnx.backend.web;

import com.google.common.base.Preconditions;
import com.google.inject.servlet.RequestScoped;
import io.fnx.backend.auth.CallContext;
import org.mint42.MintContext;
import org.mint42.resolution.RedirectResolution;
import org.mint42.resolution.Resolution;

import javax.inject.Inject;

/**
 *
 */
@RequestScoped
public class BaseController {

	@Inject
	protected CallContext callContext;

	@Inject
	protected MintContext mintContext;

	@Inject
	protected PageMeta pageMeta;

	protected Resolution redirectWithMessage(String url, String message) {
		RedirectResolution r = new RedirectResolution(url);
		r.addParameter(FlashMessages.MESSAGE, message);
		return r;
	}

	protected Resolution redirectWithError(String url, String error) {
		RedirectResolution r = new RedirectResolution(url);
		r.addParameter(FlashMessages.ERROR, error);
		return r;
	}

	protected Resolution redirect(String url) {
		return new RedirectResolution(url);
	}

	protected void assertPOST() {
		Preconditions.checkState(mintContext.getRequest().getMethod().equals("POST"), "Only POST method is allowed");
	}

}
