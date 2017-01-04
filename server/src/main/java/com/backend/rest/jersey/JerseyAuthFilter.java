package com.backend.rest.jersey;

import com.backend.auth.CallContext;
import com.backend.domain.UserEntity;
import com.backend.service.UserService;
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
        callContextProvider.get().setLoggedUser(null);

        if (token != null) {
            final UserEntity user = userService.useAuthToken(token);
            callContextProvider.get().setLoggedUser(user);
        }
        if (callContextProvider.get().logged()) {
            final UserEntity user = callContextProvider.get().getLoggedUser();
            log.info(format("Request authenticated as %s, %s [%d]", user.getName(), user.getEmail(), user.getId()));
        } else if (!isNullOrEmpty(request.getHeaderValue("X-Appengine-Cron")) ||
                   !isNullOrEmpty(request.getHeaderValue("X-AppEngine-QueueName"))) {
            log.info("Request originated from Appengine");
        } else {
            log.info("Request not authenticated or token expired");
        }
        return request;
    }

}
