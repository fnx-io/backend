package com.backend.rest.jersey;

import com.backend.auth.CallContext;
import com.backend.domain.UserEntity;
import com.backend.service.UserService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.HttpHeaders;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;

public class JerseyAuthFilter implements ContainerRequestFilter {

    private static Logger log = LoggerFactory.getLogger(JerseyAuthFilter.class.getName());

    private Provider<CallContext> callContextProvider;
    private UserService userService;

    @Inject
    public JerseyAuthFilter(Provider<CallContext> callContextProvider, UserService userService) {
        this.callContextProvider = callContextProvider;
        this.userService = userService;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {

        String token = request.getHeaderValue(HttpHeaders.AUTHORIZATION);
        final CallContext cc = callContextProvider.get();
        cc.setLoggedUser(null);

        final boolean trustedRequest = requestIsTrusted();
        cc.setTrusted(trustedRequest);

        if (trustedRequest) {
            String email = null;
            final User appengineUser = us().getCurrentUser();
            if (appengineUser != null) email = appengineUser.getEmail();
            log.info(format("Request is considered to be TRUSTED, called by user [%s]", email));
        }

        if (token != null) {
            final UserEntity user = userService.useAuthToken(token);
            cc.setLoggedUser(user);
        }
        if (cc.logged()) {
            final UserEntity user = cc.getLoggedUser();
            log.info(format("Request authenticated as %s, %s [%d]", user.getName(), user.getEmail(), user.getId()));
        } else if (!isNullOrEmpty(request.getHeaderValue("X-Appengine-Cron")) ||
                   !isNullOrEmpty(request.getHeaderValue("X-AppEngine-QueueName"))) {
            // explicitly set cron and task queue requests as trusted
            cc.setTrusted(true);
            log.info("Request originated from Appengine and is considered TRUSTED");
        } else if (!trustedRequest) {
            log.info("Request not authenticated or token expired");
        }
        return request;
    }

    /**
     * Appengine has its own notion of users.
     * They can be granted different roles in the console.
     * Selected group of them might become Administrators and then appengine
     * will protect configured endpoints by Google login screen.
     *
     * @return This function returns <code>true</code> if current request is
     * executing in the context of authenticated Appengine admin account
     */
    public static boolean requestIsTrusted() {
        final com.google.appengine.api.users.UserService service = us();
        return service.isUserLoggedIn() && service.isUserAdmin();
    }

    private static com.google.appengine.api.users.UserService us() {
        return UserServiceFactory.getUserService();
    }

}
