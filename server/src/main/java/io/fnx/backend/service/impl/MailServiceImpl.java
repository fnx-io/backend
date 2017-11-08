package io.fnx.backend.service.impl;

import com.sendgrid.SendGrid;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.service.BaseService;
import io.fnx.backend.service.MailService;
import io.fnx.backend.util.thyme.ThymeEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.StringWriter;
import java.util.Map;

public class MailServiceImpl extends BaseService implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    private TemplateEngine templateEngine;

	public MailServiceImpl() {
		this.templateEngine = ThymeEngineFactory.initializeTemplateEngine("thyme-email");
	}

	@Override
	public void sendEmail(UserEntity to, String subject, String htmlBody) {
		if (to == null) {
			log.error("Null email");
			return;
		}
		if (to.getEmail() == null) {
			log.error("User "+to+" doesn't have an email");
			return;
		}
		if (backendConfiguration.getBooleanProperty("email.skip")) {
			log.warn("Emails disabled by appengine-web.xml configuration - email.skip");
			log.info("Would send: "+subject);
			log.info(htmlBody);
			return;
		}
		log.info("Sending email '"+subject+"' to "+to.getEmail());
		SendGrid sg = new SendGrid(backendConfiguration.getProperty("email.sendGrid"));
		String from = backendConfiguration.getProperty("email.from");
		if (from == null) throw new IllegalStateException("Missing email.from in appengine-web.xml");

		SendGrid.Email email = new SendGrid.Email();
		email.setFrom(from);
		email.setTo(new String[]{to.getEmail()});
		email.setSubject(subject);
		
		try {
			email.setHtml(htmlBody);
			sg.send(email);

		} catch (Exception e) {
			log.error("Cannot send email: "+e, e);
		}
	}

	@Override
	public void sendEmail(UserEntity to, String subject, String templateName, Map<String, Object> templateParams) {
		log.info("Rendering mail template: "+templateName);
		Context ctx = new Context();
		ctx.setVariables(templateParams);
		ctx.setVariable("subject", subject);
		ctx.setVariable("root", backendConfiguration.getProperty("root"));
		StringWriter writer = new StringWriter();
		templateEngine.process(templateName, ctx, writer);
		sendEmail(to, subject, writer.toString());
	}
}
