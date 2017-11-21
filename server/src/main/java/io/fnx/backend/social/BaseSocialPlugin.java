package io.fnx.backend.social;

import com.google.gson.Gson;
import com.google.inject.Inject;
import io.fnx.backend.util.HttpRequest;
import io.fnx.backend.util.NameValuePair;
import io.fnx.backend.util.conf.BackendConfiguration;
import org.mint42.util.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Zakladni plugin pro prihlasovani pres socialni site
 *
 * @author Pecina Ondrej <pecina.ondrej@gmail.com>
 */
public abstract class BaseSocialPlugin<PERMISSION extends SocialPermission> implements SocialPlugin<PERMISSION> {

	protected Gson gson;
	private Set<PERMISSION> userPermissions;

	@Inject
	protected BackendConfiguration backendConfiguration;
	

	protected Logger log = LoggerFactory.getLogger(getClass().getName());

	public BaseSocialPlugin(Gson gson) {
		this.gson = gson;
	}

	@Override
	public void addPermission(PERMISSION permission) {
		if (userPermissions == null) {
			//noinspection Convert2Diamond
			userPermissions = new HashSet<>();
		}

		userPermissions.add(permission);
	}

	abstract protected String getAppId();

	abstract protected String getAppSecret();

	/**
	 * Polozi dotaz na URL, vrati response v body
	 *
	 * @param url
	 * @return - response serveru
	 */
	protected String fetchUrl(Url url) {
        return processBody(HttpRequest.get(url.toString()).body());
	}

	/**
	 * Polozi POST dotaz s post daty na url, response vraci v body
	 *
	 * @param url
	 * @param paramsMap - seznam dat k postu
	 * @return - response serveru
	 */
	protected String postUrl(Url url, Map<String, String> paramsMap) {
		try {
			List<NameValuePair> postParameters = new ArrayList<>();

			for (String s : paramsMap.keySet()) {
				postParameters.add(new NameValuePair(s, paramsMap.get(s)));
			}

			return processBody(HttpRequest.post(url.toString())
				.send(getQuery(postParameters))
				.body());

		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	protected String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params)
		{
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	protected String processBody(String body) {
		if (body.indexOf("error") > 0) {
			Errors errors = gson.fromJson(body, Errors.class);
			throw new IllegalStateException("Plugin error: " + errors.getError().getMessage() + " [" + errors.getError().getCode() + "]");
		}
		return body;
	}

	protected String getStamp() {
		return backendConfiguration.getProperty("root").hashCode()+""+backendConfiguration.isProduction();
	}

	/**
	 * Vraci serializovane prava, oddelena carkou
	 *
	 * @return - serializovana prava
	 */
	protected String getPermissions() {
		StringBuilder builder = new StringBuilder();
		String sep = "";

		for (PERMISSION userPermission : userPermissions) {
			builder.append(sep);
			builder.append(userPermission.getScopeName());
			sep = ",";
		}
		return builder.toString();
	}

	/**
	 * Chyba vyvolana OAuth Login API
	 */
	protected static class Errors {

		public Error error;

		public Error getError() {
			return error;
		}

		public void setError(Error error) {
			this.error = error;
		}

		public static class Error {
			private String message;
			private Long code;

			public String getMessage() {
				return message;
			}

			public void setMessage(String message) {
				this.message = message;
			}

			public Long getCode() {
				return code;
			}

			public void setCode(Long code) {
				this.code = code;
			}
		}
	}
}
