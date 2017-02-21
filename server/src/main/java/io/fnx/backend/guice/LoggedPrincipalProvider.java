package io.fnx.backend.guice;

import io.fnx.backend.auth.CallContext;
import io.fnx.backend.tools.auth.Principal;

import javax.inject.Inject;
import javax.inject.Provider;

public class LoggedPrincipalProvider implements Provider<Principal> {

    private Provider<CallContext> ccProvider;

    @Override
    public Principal get() {
        return ccProvider.get().getLoggedUser();
    }

    @Inject
    public void setCcProvider(Provider<CallContext> ccProvider) {
        this.ccProvider = ccProvider;
    }
}
