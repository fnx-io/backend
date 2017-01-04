package com.backend.service;

import com.backend.auth.CallContext;
import com.googlecode.objectify.Objectify;
import io.fnx.backend.tools.ofy.OfyProvider;

import javax.inject.Inject;
import javax.inject.Provider;

public class BaseService {

    private OfyProvider ofyProvider;
    private Provider<CallContext> callContextProvider;

    public Objectify ofy() {
        return ofyProvider.get();
    }

    public CallContext cc() {
        return callContextProvider.get();
    }

    @Inject
    public void setOfyProvider(OfyProvider ofyProvider) {
        this.ofyProvider = ofyProvider;
    }

    @Inject
    public void setCallContextProvider(Provider<CallContext> callContextProvider) {
        this.callContextProvider = callContextProvider;
    }
}
