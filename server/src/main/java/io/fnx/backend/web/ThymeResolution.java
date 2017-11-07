package io.fnx.backend.web;

import com.google.inject.Inject;
import io.fnx.backend.guice.GuiceConfig;
import io.fnx.backend.util.thyme.JodaDateThymeDialect;
import io.fnx.backend.util.thyme.MaThymeDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.mint42.MintContext;
import org.mint42.config.LocalePicker;
import org.mint42.resolution.Resolution;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomas Zverina <zverina@m-atelier.cz>
 */
public class ThymeResolution implements Resolution {

	private static TemplateEngine templateEngine;
	
	@Inject
	private LocalePicker localePicker;

	@Inject
	private PageMeta pageMeta;

	static {
		initializeTemplateEngine();
	}

	private String template;

	public ThymeResolution(String template) {
		this.template = template;

		// trochu slabsi DI
		GuiceConfig.getInjectorInstance().injectMembers(this);
	}

	@Override
	public void render(MintContext mintContext) throws IOException, ServletException {
		HttpServletResponse response = mintContext.getResponse();
		HttpServletRequest request = mintContext.getRequest();
		mintContext.getRequest().setAttribute("showNotImplemented", false);
		WebContext tctx = new WebContext(mintContext.getRequest(), response, request.getSession().getServletContext(), localePicker.getLocale(mintContext));
		tctx.setVariable("callContext", mintContext);
		tctx.setVariable("dateFormat", "dd MMMM yyyy");
		tctx.setVariable("pageMeta", pageMeta);
		//tctx.setVariable("dateFormat", messageAccessor.getMessage("app.dateFormat"));

		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		templateEngine.process(template, tctx, response.getWriter());
	}

	private static void initializeTemplateEngine() {
		ServletContextTemplateResolver templateResolver =  new ServletContextTemplateResolver();
		// XHTML is the default mode, but we set it anyway for better understanding of code
		templateResolver.setTemplateMode("LEGACYHTML5");
		// This will convert "home" to "/WEB-INF/templates/home.html"
		templateResolver.setPrefix("/WEB-INF/classes/thyme-web/");
		templateResolver.setCacheable(false);
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding("UTF-8");
		// Template cache TTL=1h. If not set, entries would be cached until expelled by LRU
		templateResolver.setCacheTTLMs(3600000L);
		
		templateEngine = new TemplateEngine();
		templateEngine.addDialect(new LayoutDialect());
		templateEngine.addDialect(new MaThymeDialect());
		templateEngine.addDialect(new JodaDateThymeDialect());
		templateEngine.setTemplateResolver(templateResolver);
	}
}
