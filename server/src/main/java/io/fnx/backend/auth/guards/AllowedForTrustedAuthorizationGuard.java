package io.fnx.backend.auth.guards;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import io.fnx.backend.auth.CallContext;
import io.fnx.backend.auth.guards.AllowedForTrusted;
import io.fnx.backend.tools.auth.Principal;
import io.fnx.backend.tools.auth.PrincipalRole;
import io.fnx.backend.tools.authorization.AuthorizationGuard;
import io.fnx.backend.tools.authorization.AuthorizationResult;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Checks, if the call context is trusted
 */
public class AllowedForTrustedAuthorizationGuard implements AuthorizationGuard {

    public static final List<Class<? extends Annotation>> annotations;

    static {
        final LinkedList<Class<? extends Annotation>> c = Lists.newLinkedList();
        c.add(AllowedForTrusted.class);
        annotations = Collections.unmodifiableList(c);
    }

    private Provider<CallContext> ccProvider;

    @Override
    public Collection<Class<? extends Annotation>> getAnnotationClasses() {
        return annotations;
    }

    @Override
    public AuthorizationResult guardInvocation(MethodInvocation invocation, Annotation annotation, PrincipalRole callingRole, Key<? extends Principal> callingPrincipal) {
        boolean trusted = ccProvider != null && ccProvider.get() != null && ccProvider.get().isTrusted();
        if (trusted) {
            return AuthorizationResult.SUCCESS;
        } else {
            return AuthorizationResult.failure("Context not trusted!");
        }
    }

    @Inject
    public void setCcProvider(Provider<CallContext> ccProvider) {
        this.ccProvider = ccProvider;
    }
}
