package io.fnx.backend.web;

import com.google.inject.Inject;
import io.fnx.backend.util.conf.BackendConfiguration;
import org.mint42.MintContext;
import org.mint42.annotations.On;
import org.mint42.exception.ResolutionVetoException;
import org.mint42.resolution.RedirectResolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by venash on 25.09.17.
 */
public class HttpsRedirect {

    private static final Logger log = LoggerFactory.getLogger(HttpsRedirect.class);

    protected BackendConfiguration backendConfiguration;

    @Inject
    public void setBackendConfiguration(BackendConfiguration backendConfiguration) {
        this.backendConfiguration = backendConfiguration;
    }

    @On(stage = On.Stage.INIT)
    public void enforceHttp(MintContext ctx, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean useHttpsRedirect = backendConfiguration.getBooleanProperty("use.https.redirect");
        if (useHttpsRedirect) {
            String protocol = ctx.getRequest().getScheme();
            if ("http".equals(protocol)) {
                StringBuffer url = ctx.getRequest().getRequestURL();
                String queryParams = ctx.getRequest().getQueryString();
                String finalUrl = url.toString().replaceAll("^http:","https:");
                if (queryParams != null) {
                    finalUrl = finalUrl.concat("?").concat(queryParams);
                }
                log.info("Redirecting to HTTPS: "+finalUrl);
                throw new ResolutionVetoException(new RedirectResolution(finalUrl));
            }
        }
    }


}
