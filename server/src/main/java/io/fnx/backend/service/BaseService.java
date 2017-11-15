package io.fnx.backend.service;

import io.fnx.backend.auth.CallContext;
import com.googlecode.objectify.Objectify;
import io.fnx.backend.tools.ofy.OfyProvider;
import io.fnx.backend.util.MessageAccessor;
import io.fnx.backend.util.conf.BackendConfiguration;

import javax.inject.Inject;
import javax.inject.Provider;

@Service
public class BaseService {

    private OfyProvider ofyProvider;
    private Provider<CallContext> callContextProvider;
    protected BackendConfiguration backendConfiguration;
    protected MessageAccessor messageAccessor;

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

    @Inject
	public void setBackendConfiguration(BackendConfiguration backendConfiguration) {
		this.backendConfiguration = backendConfiguration;
	}

	@Inject
	public void setMessageAccessor(MessageAccessor messageAccessor) {
		this.messageAccessor = messageAccessor;
	}

	protected void randomSleep() {
		try {
			Thread.sleep((long) (Math.random()*100)+10);
		} catch (InterruptedException e) { }
	}

}
