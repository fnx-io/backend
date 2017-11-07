package io.fnx.backend.util.thyme;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.fnx.backend.guice.GuiceConfig;
import org.mint42.MintContext;
import org.mint42.config.LocalePicker;
import org.mint42.config.MessageProvider;
import org.thymeleaf.Arguments;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;
import org.thymeleaf.processor.attr.AbstractUnescapedTextChildModifierAttrProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Pecina Ondrej <pecina.ondrej@gmail.com>
 * @author Tomas Zverina <zverina@m-atelier.cz>
 */
public class MaThymeDialect extends AbstractDialect {

	public static final String DIALECT_NAMESPACE_MA = "http://www.m-atelier.cz/thymeleaf/ma";
	public static final String DIALECT_PREFIX_MA = "ma";

	private static final int PRECEDENCE = 10000;

	@Inject
	private Provider<MintContext> callContextProvider;

	@Inject
	private MessageProvider messages;

	@Inject
	private LocalePicker localePicker;

	public MaThymeDialect() {}

	public String getPrefix() {
		return DIALECT_PREFIX_MA;
	}

	public Set<IProcessor> getProcessors() {
		HashSet processors = new HashSet();
		processors.add(new I18nHrefTextProcessor());
		processors.add(new I18nTextProcessor());
		processors.add(new I18nAltTextProcessor());
		processors.add(new I18nTitleTextProcessor());
		processors.add(new I18nPlaceholderTextProcessor());
		processors.add(new I18nValueTextProcessor());
		return processors;
	}

	public String getMessageText(String code) {

		if (messages == null) {
			GuiceConfig.getInjectorInstance().injectMembers(MaThymeDialect.this);
		}

		try {
			return messages.renderMessage(localePicker.getLocale(callContextProvider.get()), code, null);
		} catch (java.util.MissingResourceException e) {
			// ok, chybi text
			return null;
		}
	}

	public class I18nAltTextProcessor extends AbstractAttrProcessor {

		public I18nAltTextProcessor() {
			super("alt");
		}

		public final int getPrecedence() {
			return PRECEDENCE;
		}

		protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
			if (callContextProvider == null) {
				GuiceConfig.getInjectorInstance().injectMembers(MaThymeDialect.this);
			}
			String code = element.getAttributeValue(attributeName);
			String newText = getMessageText(code);

			String originalContent = element.getAttributeValue("alt");
			if (newText == null) {
				System.out.println(code+"="+originalContent.trim().replaceAll("\n", "\\n"));
				element.setAttribute("style", "color: red");
				return ProcessorResult.OK;
			}
			element.setAttribute("alt", newText);

			element.removeAttribute(attributeName);

			return ProcessorResult.OK;
		}
	}

	public class I18nValueTextProcessor extends AbstractAttrProcessor {

		public I18nValueTextProcessor() {
			super("value");
		}

		public final int getPrecedence() {
			return PRECEDENCE;
		}

		protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
			if (callContextProvider == null) {
				GuiceConfig.getInjectorInstance().injectMembers(MaThymeDialect.this);
			}
			String code = element.getAttributeValue(attributeName);
			String newText = getMessageText(code);

			String originalContent = element.getAttributeValue("value");
			if (newText == null) {
				System.out.println(code+"="+originalContent.trim().replaceAll("\n", "\\n"));
				element.setAttribute("style", "color: red");
				return ProcessorResult.OK;
			}
			element.setAttribute("value", newText);

			element.removeAttribute(attributeName);

			return ProcessorResult.OK;
		}
	}

	public class I18nPlaceholderTextProcessor extends AbstractAttrProcessor {

		public I18nPlaceholderTextProcessor() {
			super("placeholder");
		}

		public final int getPrecedence() {
			return PRECEDENCE;
		}

		protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
			if (callContextProvider == null) {
				GuiceConfig.getInjectorInstance().injectMembers(MaThymeDialect.this);
			}
			String code = element.getAttributeValue(attributeName);
			String newText = getMessageText(code);

			String originalContent = element.getAttributeValue("placeholder");
			if (newText == null) {
				System.out.println(code+"="+originalContent.trim().replaceAll("\n", "\\n"));
				element.setAttribute("style", "color: red");
				return ProcessorResult.OK;
			}
			element.setAttribute("placeholder", newText);

			element.removeAttribute(attributeName);

			return ProcessorResult.OK;
		}
	}


	public class I18nHrefTextProcessor extends AbstractAttrProcessor {

		public I18nHrefTextProcessor() {
			super("href");
		}

		public final int getPrecedence() {
			return PRECEDENCE;
		}

		protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
			if (callContextProvider == null) {
				GuiceConfig.getInjectorInstance().injectMembers(MaThymeDialect.this);
			}
			String code = element.getAttributeValue(attributeName);
			String newText = getMessageText(code);

			String originalContent = element.getAttributeValue("href");
			if (newText == null) {
				System.out.println(code+"="+originalContent.trim().replaceAll("\n", "\\n"));
				element.setAttribute("style", "color: red");
				return ProcessorResult.OK;
			}
			element.setAttribute("href", localePicker.getLocale(callContextProvider.get())+"/"+newText);

			element.removeAttribute(attributeName);

			return ProcessorResult.OK;
		}
	}

	public class I18nTitleTextProcessor extends AbstractAttrProcessor {

		public I18nTitleTextProcessor() {
			super("title");
		}

		public final int getPrecedence() {
			return PRECEDENCE;
		}

		protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
			if (callContextProvider == null) {
				GuiceConfig.getInjectorInstance().injectMembers(MaThymeDialect.this);
			}
			String code = element.getAttributeValue(attributeName);
			String newText = getMessageText(code);

			String originalContent = element.getAttributeValue("title");
			if (newText == null) {
				System.out.println(code+"="+originalContent.trim().replaceAll("\n", "\\n"));
				element.setAttribute("style", "color: red");
				return ProcessorResult.OK;
			}
			element.setAttribute("alt", newText);

			element.removeAttribute(attributeName);

			return ProcessorResult.OK;
		}
	}


	public class I18nTextProcessor extends AbstractUnescapedTextChildModifierAttrProcessor {

		public I18nTextProcessor() {
			super("text");
		}

		public final int getPrecedence() {
			return PRECEDENCE;
		}

		@Override
		protected String getText(Arguments arguments, Element element, String attributeName) {
			String code = element.getAttributeValue(attributeName);


			String newText = getMessageText(code);

			if (newText == null) {
				String originalContent = ((Text)element.getFirstChild()).getContent();
				//if (element.getChildren().size() != 1) throw new IllegalStateException("ma:text nodes can contain only one children node - text ("+code+")");
				//if (!(element.getFirstChild() instanceof Text)) throw new IllegalStateException("ma:text node must contain only text ("+code+")");
				System.out.println(code+"="+originalContent.trim().replaceAll("\n", "\\n"));
				element.setAttribute("style", "color: red");
				return "(!) "+originalContent;
			}

			return newText;
		}

	}
}