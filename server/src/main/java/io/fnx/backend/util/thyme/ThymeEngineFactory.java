package io.fnx.backend.util.thyme;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.io.InputStream;

/**
 * @author Tomas Zverina <zverina@m-atelier.cz>
 */
public class ThymeEngineFactory {

	public static TemplateEngine initializeTemplateEngine(final String thymeFolder) {
		TemplateResolver templateResolver =  new TemplateResolver();
		// XHTML is the default mode, but we set it anyway for better understanding of code
		templateResolver.setTemplateMode("LEGACYHTML5");
		templateResolver.setCacheable(true);
		templateResolver.setCharacterEncoding("UTF-8");
		// Template cache TTL=1h. If not set, entries would be cached until expelled by LRU
		templateResolver.setCacheTTLMs(3600000L);

		templateResolver.setResourceResolver(new FnxResourceResolver(thymeFolder));

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.addDialect(new LayoutDialect());
		templateEngine.addDialect(new MaThymeDialect());
		templateEngine.addDialect(new JodaDateThymeDialect());
		templateEngine.setTemplateResolver(templateResolver);

		return templateEngine;
	}

	private static class FnxResourceResolver implements IResourceResolver {
		private final String thymeFolder;

		public FnxResourceResolver(String thymeFolder) {
			this.thymeFolder = thymeFolder;
		}

		@Override
		public String getName() {
			return "ThymeEngineFactory";
		}

		@Override
		public InputStream getResourceAsStream(TemplateProcessingParameters templateProcessingParameters, String resourceName) {
			return ThymeEngineFactory.class.getResourceAsStream("/"+ thymeFolder +"/"+resourceName+".html");
		}
	}
}