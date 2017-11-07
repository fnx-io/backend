package io.fnx.backend.web;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.mint42.MintContext;
import org.mint42.config.LocalePicker;

import java.util.*;

/**
 * Web page meta data.
 *
 */
@RequestScoped
public final class PageMeta {

	private static final Map<MetaTag, MetaTagValue> metaTagsDefaults = new TreeMap<>();

	private final Map<MetaTag, MetaTagValue> metaTagsMap = new TreeMap<>();

	@Inject
	public PageMeta(LocalePicker localePicker, MintContext ctx) {
		for (MetaTag metaTag : metaTagsDefaults.keySet()) {
			setMetaTagValue(metaTag, metaTagsDefaults.get(metaTag).value);
		}
		setMetaTagValue(MetaTag.OG_LOCALE, localePicker.getLocale(ctx).toString());
	}

	public void setMetaTagValue(MetaTag metaTag, String value) {
		MetaTagValue metaTagValue = new MetaTagValue(metaTag, value, this);
		metaTagsMap.put(metaTag, metaTagValue);
	}

	public static void setMetaTagsDefault(MetaTag metaTag, String value) {
		MetaTagValue metaTagValue = new MetaTagValue(metaTag, value, null);
		metaTagsDefaults.put(metaTag, metaTagValue);
	}

	public MetaTagValue getTag(MetaTag metaTag) {
		MetaTagValue toReturn = metaTagsMap.get(metaTag);
		if (toReturn == null) {
			throw new IllegalStateException("This cannot happen");
		}
		return toReturn;
	}

	/**
	 * Vrati kolekci vsech meta tagu, ktere je pak mozne vyrenderovat do stranky.
	 *
	 * @return
	 */
	public Collection<MetaTagValue> getMetaTags() {
		return Collections.unmodifiableCollection(metaTagsMap.values());
	}

	private static final String NAME = "name";
	private static final String PROPERTY = "property";

	/**
	 * Enum, ktery popisuje, jake vsechny meta tagy zname a jak spolu souvisi.
	 */
	public static enum MetaTag {

		TITLE(true, "title", null, null),
		DESCRIPTION(false, "description", NAME, null),
		KEYWORDS(false, "keywords", NAME, null),
		AUTHOR(false, "author", NAME, null),
		ROBOTS(false, "robots", NAME, null),
		THUMBNAIL(false, "thumbnail", NAME, null),

		// http://ogp.me/
		OG_TITLE(false, "og:title", PROPERTY, TITLE),
		OG_TYPE(false, "og:type", PROPERTY, null),
		OG_IMAGE(false, "og:image", PROPERTY, null),
		OG_URL(false, "og:url", PROPERTY, null),

		OG_DESCRIPTION(false, "og:description", PROPERTY, DESCRIPTION),
		OG_AUDIO(false, "og:audio", PROPERTY, null),
		OG_DETERMINER(false, "og:determiner", PROPERTY, null),
		OG_VIDEO(false, "og:video", PROPERTY, null),
		OG_SITE_NAME(false, "og:site_name", PROPERTY, null),
		OG_LOCALE(false, "og:locale", PROPERTY, null),

		FB_APP_ID(false, "fb:app_id", PROPERTY, null),

		TWITTER_CARD(false, "twitter:card", NAME, null),
		TWITTER_SITE(false, "twitter:site", NAME, OG_SITE_NAME),
		TWITTER_URL(false, "twitter:url", NAME, OG_URL),
		TWITTER_TITLE(false, "twitter:title", NAME, OG_TITLE),
		TWITTER_CREATOR(false, "twitter:creator", NAME, null), // TWITTER HANDLE, not simple name
		TWITTER_IMAGE(false, "twitter:image", NAME, OG_IMAGE);

		boolean renderAsTag;
		String name;
		String type;
		MetaTag inheritsFrom;

		private MetaTag(boolean renderAsTag, String name, String type, MetaTag inheritsFrom) {
			this.renderAsTag = renderAsTag;
			this.name = name;
			this.type = type;
			this.inheritsFrom = inheritsFrom;
		}
	}

	/**
	 * Konkretni hodnota jednoho meta tagu.
	 */
	public static class MetaTagValue {

		private final PageMeta parent;
		private final MetaTag tag;
		private final String value;

		public MetaTagValue(MetaTag tag, String value, PageMeta parent) {
			this.tag = tag;
			this.value = value;
			this.parent = parent;
		}

		public String getHtmlTag() {
			if (getValue() == null) return "";
			if (tag.renderAsTag) {
				return "<" + tag.name + ">" + HtmlUtil.encode(getValue()) + "</" + tag.name + ">";
			} else {
				return "<meta " + tag.type + "=\"" + tag.name + "\" content=\"" + HtmlUtil.encode(getValue()).replace('"', '\'') + "\"/>";
			}
		}

		public String getValue() {
			if (value == null && tag.inheritsFrom != null) {
				// pokud nemam hodnotu, ale muzu ji nabrat z jineho tagu, tak ji beru
				return parent.getTag(tag.inheritsFrom).getValue();
			}
			return value;
		}
	}

}