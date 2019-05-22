package io.fnx.backend.guice;

import io.fnx.backend.social.web.FacebookSocialController;
import io.fnx.backend.social.web.GoogleSocialController;
import io.fnx.backend.web.*;
import ognl.OgnlRuntime;
import org.mint42.MintModule;

/**
 * Guice module which configures Jersey to server our Rest endpoints
 */
public class WebModule extends MintModule {

	@Override
	protected void configureRouting() {
		OgnlRuntime.setSecurityManager(null);

		// TODO: add more defaults, customize later, see PagesController
		PageMeta.setMetaTagsDefault(PageMeta.MetaTag.AUTHOR, "fnx.io s.r.o.");

		// TODO: add your routes
		route(".*", HttpsRedirect.class);
		route(".*", FlashMessages.class);
		route(".*", UserController.class);

		route("/", PagesController.class, "index");

		route("/logout", UserController.class, "doLogout");
		route("/login", UserController.class, "doLogin");
		route("/register", UserController.class, "doRegister");

		route("/forgotten/request", UserController.class, "doForgottenRequest");
		route("/forgotten/send", UserController.class, "doForgottenSend");
		route("/forgotten/change", UserController.class, "doForgottenChange");
		route("/forgotten/save", UserController.class, "doForgottenSave");


		route("/social/google/redirect", GoogleSocialController.class, "doRedirect");
		route("/social/google/return", GoogleSocialController.class, "doReturn");
		route("/social/facebook/redirect", FacebookSocialController.class, "doRedirect");
		route("/social/facebook/return", FacebookSocialController.class, "doReturn");

		
		/*
		hospiceVCR.addChild("/(?<id>[A-Za-z0-9-]+)", HospiceVCrController.class, "detail");
		Route news = route("/novinky", NewsController.class, "init");
		news.addChild("/(?<id>[0-9]+)", NewsController.class, "detail");
		Route vyzvy = route("/vyzvy", CallsController.class, "init");
		vyzvy.addChild("/(?<id>[A-Za-z0-9-]+)", CallsController.class, "detail");
		*/
	}


}