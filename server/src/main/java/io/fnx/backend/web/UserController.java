package io.fnx.backend.web;

import com.google.inject.servlet.RequestScoped;
import io.fnx.backend.auth.CallContext;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.service.UserService;
import org.mint42.MintContext;
import org.mint42.annotations.On;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by venash on 25.09.17.
 */
@RequestScoped
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Inject
    private Provider<CallContext> callContextProvider;

    @Inject
    private UserService userService;
	
    @On(stage = On.Stage.PRECONDITIONS)
    public void initCC(MintContext ctx, HttpServletRequest req, HttpServletResponse resp) {
        String authKey = WebTool.getCookieValue(req, "fernet");
        if (authKey != null) {
            final UserEntity user = userService.useAuthToken(authKey);
            if (user != null) {
	            log.info("Request by "+user);
                callContextProvider.get().setLoggedUser(user);
            }
        }
    }

}
