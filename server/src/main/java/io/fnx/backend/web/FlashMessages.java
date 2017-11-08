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
 *
 */
public class FlashMessages {

	public static final String MESSAGE = "_message";
	public static final String ERROR = "_error";
	
    @On(stage = On.Stage.INIT)
    public void rebuildMessages(MintContext ctx, HttpServletRequest req) {
    	if (req.getParameter(MESSAGE) != null) {
    		ctx.getMessages().addMessage(req.getParameter(MESSAGE));
	    }
	    if (req.getParameter(ERROR) != null) {
		    ctx.getErrors().addMessage(req.getParameter(ERROR));
	    }
    }


}
