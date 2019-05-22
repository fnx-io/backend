package io.fnx.backend.web;

import com.google.inject.servlet.RequestScoped;
import org.mint42.MintContext;
import org.mint42.annotations.OnAction;
import org.mint42.resolution.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class PagesController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(PagesController.class);

	@OnAction("index")
	public Resolution index(MintContext ctx) {
		log.info("Serving index");

		pageMeta.setMetaTagValue(PageMeta.MetaTag.TITLE, "fnx backed!");

		return new ThymeResolution("index");
	}

}
