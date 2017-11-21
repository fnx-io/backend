package io.fnx.backend.web;

import com.google.inject.servlet.RequestScoped;
import io.fnx.backend.auth.CallContext;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.domain.dto.login.LoginResult;
import io.fnx.backend.domain.dto.login.UserLoginDto;
import io.fnx.backend.domain.dto.user.PasswordChangeDto;
import io.fnx.backend.domain.dto.user.UserDto;
import io.fnx.backend.manager.AuthTokenManager;
import io.fnx.backend.manager.UniqueViolationException;
import io.fnx.backend.service.UserService;
import org.mint42.MintContext;
import org.mint42.annotations.*;
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
	public Resolution login(@RequestBean("login") UserLoginDto userLogin) {
		assertPOST();

		LoginResult user = userService.login(userLogin.getEmail(), userLogin.getPassword(), false);
		
		if (user.isSuccess()) {
			loginImpl(mintContext, callContext, user.getToken(), user.getUser());
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
		loginImpl(mintContext, callContext, token, user);
		return redirectWithMessage("/","message.registered");
	}


	@OnAction("doLogout")
	public Resolution logOut() {
		if (callContext.logged()) {
			String authKey = WebTool.getCookieValue(mintContext.getRequest(), FRONTEND_SESSION_ID);
			WebTool.deleteCookie(mintContext.getResponse(), FRONTEND_SESSION_ID);
			userService.logout(authKey);
			callContext.setLoggedUser(null);
		}
		return redirectWithMessage("/", "message.loggedOut");
	}

	@OnAction("doForgottenRequest")
	public Resolution forgottenRequest() {
		return new ThymeResolution("forgotten-request");
	}

	@OnAction("doForgottenSend")
	public Resolution forgottenSend(@RequestBean("password") UserLoginDto userLogin) {
		userService.generateForgottenPasswordToken(userLogin.getEmail());
		return redirectWithMessage("/", "message.forgottenMaybeSent");
	}

	@OnAction("doForgottenChange")
	public Resolution forgottenChange(@RequestBean("change") PasswordChangeDto change) {
		mintContext.getRequest().setAttribute("change", change);
		return new ThymeResolution("forgotten-change");
	}

	@OnAction("doForgottenSave")
	public Resolution forgottenSave(@RequestBean("change") @Validate PasswordChangeDto change, ExecutionStep step) {
		assertPOST();
		mintContext.getRequest().setAttribute("change", change);
		if (step.hasBindingErrors()) {
			// nevalidni form
			mintContext.getErrors().addMessage("error.formErrors");
			return new ThymeResolution("forgotten-change");
		}

		boolean result = userService.changeForgottenPassword(change);

		if (result) {
			return redirectWithMessage("/", "message.forgottenPasswordChanged");
		} else {
			return redirectWithMessage("/", "message.forgottenPasswordNotChanged");
		}
	}

	/**
	 * Nastavi cookie a call context, takze je uzivatel prihlaseny.
	 *
	 * @param mintContext
	 * @param ctx
	 * @param authTokenFromManager
	 * @param user
	 */
	public static void loginImpl(MintContext mintContext, CallContext ctx, String authTokenFromManager, UserEntity user) {
		WebTool.setCookieValue(mintContext.getResponse(), FRONTEND_SESSION_ID, authTokenFromManager, SESSION_COOKIE_DURATION);
		ctx.setLoggedUser(user);
	}

}
