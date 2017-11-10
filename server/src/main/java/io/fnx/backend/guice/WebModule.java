package io.fnx.backend.guice;

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

		/*
		route("/pravidla", PagesController.class, "pravidla");
		route("/vyherci", PagesController.class, "vyherci");
		route("/90-let-fernetu-stock", PagesController.class, "90-let-fernetu-stock");
		Route prihlaseni = route("/prihlaseni", PagesController.class, "prihlaseni");
		route("/soutezit", PagesController.class, "soutezit");

		route("/submit", CompetitionController.class, "submit");
		route("/register", CompetitionController.class, "register");
		route("/login", CompetitionController.class, "login");

		prihlaseni.addChild("/forgotten-password", CompetitionController.class, "zapomenute-heslo");
		route("/new-password", CompetitionController.class, "new-password");

		route("/logout", CompetitionController.class, "logout-action");
		*/
		
		/*
		route(".*", PageMetaDecorator.class);

		route("/kalendar-behu", CalendarController.class, "init");
		Route hospiceVCR = route("/hospice-v-cr", HospiceVCrController.class, "init");
		hospiceVCR.addChild("/(?<id>[A-Za-z0-9-]+)", HospiceVCrController.class, "detail");

		Route news = route("/novinky", NewsController.class, "init");
		news.addChild("/(?<id>[0-9]+)", NewsController.class, "detail");

		Route vyzvy = route("/vyzvy", CallsController.class, "init");
		vyzvy.addChild("/(?<id>[A-Za-z0-9-]+)", CallsController.class, "detail");

		route("/detail-behu/(?<id>[0-9]+)", RunDetailController.class, "detail");
		route("/detail-bezce/(?<id>[0-9]+)", AboutMeController.class, "init");

		route("/hitparada", HitparadaController.class, "init");
		route("/o-projektu", OtherInfoController.class, "init");
		*/
	}


}