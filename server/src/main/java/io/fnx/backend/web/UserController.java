package io.fnx.backend.web;

import com.google.inject.servlet.RequestScoped;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.domain.dto.login.LoginResult;
import io.fnx.backend.domain.dto.login.UserLoginDto;
import io.fnx.backend.domain.dto.user.UserDto;
import io.fnx.backend.manager.AuthTokenManager;
import io.fnx.backend.manager.UniqueViolationException;
import io.fnx.backend.service.UserService;
import org.mint42.MintContext;
import org.mint42.annotations.On;
import org.mint42.annotations.OnAction;
import org.mint42.annotations.RequestBean;
import org.mint42.annotations.Validate;
import org.mint42.execution.ExecutionStep;
import org.mint42.execution.ExecutionStepContext;
import org.mint42.messages.LocalizableFieldControllerMessage;
import org.mint42.resolution.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by venash on 25.09.17.
 */
@RequestScoped
public class UserController extends BaseController {

	private static final String FRONTEND_SESSION_ID = "fsid";
	private static final int SESSION_COOKIE_DURATION = 50;

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Inject
	private UserService userService;

	@Inject
	private AuthTokenManager<UserEntity> tokenManager;

	@On(stage = On.Stage.PRECONDITIONS)
	public void initCC() {
		String authKey = WebTool.getCookieValue(mintContext.getRequest(), FRONTEND_SESSION_ID);
		if (authKey != null) {
			final UserEntity user = userService.useAuthToken(authKey);
			if (user != null) {
				log.info("Request by "+user);
				callContext.setLoggedUser(user);
			}
		}
	}

	@OnAction("doLogin")
	public Resolution login(@RequestBean UserLoginDto userLogin) {
		assertPOST();

		LoginResult user = userService.login(userLogin.getEmail(), userLogin.getPassword(), false);
		
		if (user.isSuccess()) {
			WebTool.setCookieValue(mintContext.getResponse(), FRONTEND_SESSION_ID, user.getToken(), SESSION_COOKIE_DURATION);
			callContext.setLoggedUser(user.getUser());
			return redirectWithMessage("/", "message.loggedIn");

		} else {
			mintContext.getErrors().addMessage("error.wrongCredentials");
			return new ThymeResolution("index");
		}
	}

	/**
	 * Execution step se vaze na tohle konkretni volani tenhle konkretni metody. Napriklad tedy obsahuje
	 * validacni chyby vznikle z @RequestBean("register")
	 *
	 * 
	 * @param userRegister
	 * @param step
	 * @return
	 */
	@OnAction("doRegister")
	public Resolution register(@RequestBean("register") @Validate UserDto userRegister, ExecutionStep step) {
		assertPOST();

		if (step.hasBindingErrors()) {
			// nevalidni form
			mintContext.getErrors().addMessage("error.formErrors");
			return new ThymeResolution("index");
		}

		UserEntity user;
		try {
			user = userService.registerUser(userRegister);
		} catch (UniqueViolationException e) {
			mintContext.getErrors().addMessage("error.email.duplicate");
			return new ThymeResolution("index");
		}

		String token = tokenManager.newAuthTokenFor(user);
		WebTool.setCookieValue(mintContext.getResponse(), FRONTEND_SESSION_ID, token, SESSION_COOKIE_DURATION);
		callContext.setLoggedUser(user);
		return redirectWithMessage("/","message.registered");
	}


	@OnAction("doLogout")
	public Resolution logOut(MintContext ctx) {
		if (callContext.logged()) {
			String authKey = WebTool.getCookieValue(ctx.getRequest(), FRONTEND_SESSION_ID);
			WebTool.deleteCookie(ctx.getResponse(), FRONTEND_SESSION_ID);
			userService.logout(authKey);
			callContext.setLoggedUser(null);
		}
		return redirectWithMessage("/", "message.loggedOut");
	}


}
