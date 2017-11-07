package io.fnx.backend.util.thyme;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.fnx.backend.guice.GuiceConfig;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mint42.MintContext;
import org.mint42.config.LocalePicker;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.*;


/**
 * @author Venash
 */
public class JodaDateThymeDialect extends AbstractDialect implements IExpressionEnhancingDialect {

	/**
	 * Basic configuration
	 */
	public static final String JODA = "joda";

	@Inject
	private LocalePicker localePicker;

	@Inject
	private Provider<MintContext> callContextProvider;


	@Override
	public String getPrefix() {
		return JODA;
	}

	/**
	 * Nastaveni procesoru
	 * @return
     */

	@Override
	public Set<IProcessor> getProcessors() {
		Set<IProcessor> processors = new HashSet<IProcessor>();
		processors.add(new ConvertJodaDateTimeProcessor("convert", DateTimeFormat.fullDate()));
		return processors;
	}


	@Override
	public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext iProcessingContext) {
		Map<String, Object> expressionObjects = new HashMap<String, Object>();
		if (callContextProvider == null) {
			GuiceConfig.getInjectorInstance().injectMembers(JodaDateThymeDialect.this);
		}
		expressionObjects.put(JODA, new JodaTimeExpressionObject(new Locale("cs", "CZ")));
		//expressionObjects.put(JODA, new JodaTimeExpressionObject(localePicker.getLocale(callContextProvider.get())));
		return expressionObjects;
	}


	private class ConvertJodaDateTimeProcessor extends AbstractTextChildModifierAttrProcessor {

		private static final String EMPTY = "";

		private final DateTimeFormatter formatter;

		public ConvertJodaDateTimeProcessor(String attrName, DateTimeFormatter formatter) {
			super(attrName);
			this.formatter = formatter;
		}

		@Override
		public int getPrecedence() {
			return 0;
		}

		@Override
		protected String getText(Arguments arguments, Element element, String attributeName) {

			final String attributeValue = element.getAttributeValue(attributeName);

			final Configuration configuration = arguments.getConfiguration();

			IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
			final IStandardExpression expression = parser.parseExpression(configuration, arguments, attributeValue);

			Object result = expression.execute(configuration, arguments);

			//DateTimeFormatter localFormatter = formatter.withLocale(localePicker.getLocale(callContextProvider.get()));
			DateTimeFormatter localFormatter = formatter.withLocale(new Locale("cs", "CZ"));

			return result instanceof DateTime ? localFormatter.print((DateTime) result) : EMPTY;
		}
	}

	private class JodaTimeExpressionObject {

		private final Locale locale;

		private JodaTimeExpressionObject(Locale locale) {
			this.locale = locale;
		}

		public String convert(DateTime dateTime) {
			DateTimeFormatter dtf = DateTimeFormat.longDate();
			return format(dateTime, dtf);
		}

		private String format(DateTime dateTime, DateTimeFormatter formatter) {
			return formatter.withLocale(locale).print(dateTime);
		}

	}
}