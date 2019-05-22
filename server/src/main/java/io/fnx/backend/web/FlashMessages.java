package io.fnx.backend.web;

import org.mint42.MintContext;
import org.mint42.annotations.On;

import javax.servlet.http.HttpServletRequest;

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
