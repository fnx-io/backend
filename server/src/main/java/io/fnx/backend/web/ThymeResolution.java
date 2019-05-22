package io.fnx.backend.web;

import com.google.inject.Inject;
import io.fnx.backend.auth.CallContext;
import io.fnx.backend.guice.GuiceConfig;
import io.fnx.backend.util.conf.BackendConfiguration;
import io.fnx.backend.util.thyme.ThymeEngineFactory;
import org.mint42.MintContext;
import org.mint42.config.LocalePicker;
import org.mint42.config.MessageProvider;
import org.mint42.messages.RenderedMessagesCollection;
import org.mint42.resolution.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Tomas Zverina <zverina@m-atelier.cz>
 */
public class ThymeResolution implements Resolution {

	private static final Logger log = LoggerFactory.getLogger(ThymeResolution.class);

	private static TemplateEngine templateEngine;
	
	@Inject
	private LocalePicker localePicker;

	@Inject
	private BackendConfiguration backendConfiguration;

	@Inject
	private PageMeta pageMeta;

	@Inject
	private CallContext callContext;

	@Inject
	private MessageProvider messageProvider;

	static {
		templateEngine = ThymeEngineFactory.initializeTemplateEngine("thyme-web");
	}

	private String template;

	public ThymeResolution(String template) {
		this.template = template;
		GuiceConfig.getInjectorInstance().injectMembers(this);
	}

	@Override
	public void render(MintContext mintContext) throws IOException, ServletException {
		HttpServletResponse response = mintContext.getResponse();
		HttpServletRequest request = mintContext.getRequest();
		mintContext.getRequest().setAttribute("showNotImplemented", false);
		WebContext tctx = new WebContext(mintContext.getRequest(), response, request.getSession().getServletContext(), localePicker.getLocale(mintContext));
		tctx.setVariable("cc", callContext);
		tctx.setVariable("logged", callContext.logged());
		tctx.setVariable("loggedUser", callContext.getLoggedUser());
		tctx.setVariable("mintContext", mintContext);

		RenderedMessagesCollection messages = mintContext.getMessages().renderMessages(localePicker.getLocale(mintContext), messageProvider, mintContext);
		tctx.setVariable("messages", messages);
		tctx.setVariable("messagesMap", messages.getAsMap());

		RenderedMessagesCollection errors = mintContext.getErrors().renderMessages(localePicker.getLocale(mintContext), messageProvider, mintContext);
		tctx.setVariable("errors", errors);
		tctx.setVariable("errorsMap", errors.getAsMap());
		
		tctx.setVariable("mintContext", mintContext);
		tctx.setVariable("dateFormat", "dd MMMM yyyy");
		tctx.setVariable("pageMeta", pageMeta);
		tctx.setVariable("root", backendConfiguration.getProperty("root"));
		//tctx.setVariable("dateFormat", messageAccessor.getMessage("app.dateFormat"));

		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		log.info("Rendering "+template+" template with following variables: "+ tctx.getVariables().keySet());

		templateEngine.process(template, tctx, response.getWriter());
	}

}
