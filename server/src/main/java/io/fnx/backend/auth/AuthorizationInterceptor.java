package io.fnx.backend.auth;

import com.googlecode.objectify.Key;
import io.fnx.backend.tools.auth.Principal;
import io.fnx.backend.tools.auth.PrincipalRole;
import io.fnx.backend.tools.authorization.AuthorizationGuard;
import io.fnx.backend.tools.authorization.AuthorizationResult;
import io.fnx.backend.tools.authorization.PermissionDeniedException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Main class which should be configured to intercept calls to methods you care about (manager methods?) and
 * you want it to check for authorization of these calls.
 *
 * It has simple (plugin) architecture, in that it consists of sets of {@link AuthorizationGuard}s which react to
 * different annotations and different checks.
 * <br>
 * <br>
 * Sample configuration for a Guice based application:
 * <br>
 * <br>
 * <pre>
 *     // be sure to have provider for UserContext
 *     bind(UserContext.class).toProvider(UserContextProvider.class).in(RequestScoped.class);
 *
 *     final AuthorizationInterceptor fnxAuthorizationInterceptor = new AuthorizationInterceptor();
 *     // select your own set of guards
 *     fnxAuthorizationInterceptor.setGuards(createAuthorizationGuards());
 *     requestInjection(fnxAuthorizationInterceptor);
 *     // intercept calls to manager methods and check the authorization of these calls
 *     bindInterceptor(Matchers.subclassesOf(Service.class), Matchers.any(), fnxAuthorizationInterceptor);
 * </pre>
 *
 * @see AuthorizationGuard
 * @see Principal
 */
public class AuthorizationInterceptor implements MethodInterceptor {

    private final Provider<Principal> principalProvider;
    private AuthorizationGuard[] guards = new AuthorizationGuard[0];

    public AuthorizationInterceptor(Provider<Principal> principalProvider) {
        this.principalProvider = principalProvider;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final AuthorizationResult result = runGuards(invocation);

        if (result == null || result.success) {
            return invocation.proceed();
        } else {
            throw new PermissionDeniedException(result.msg);
        }
    }

    private AuthorizationResult runGuards(MethodInvocation invocation) {
        AuthorizationResult result = null;
        int guardInvocationCount = 0;

        for (final AuthorizationGuard guard : guards) {
            // skip if authorization had already been successful
            if (result != null && result.success) continue;
            final Collection<Class<? extends Annotation>> annotationClasses = guard.getAnnotationClasses();
            if (annotationClasses == null) continue;
            for (Class<? extends Annotation> annotationClass : annotationClasses) {
                if (annotationClass == null) continue;
                final Annotation annotation = invocation.getMethod().getAnnotation(annotationClass);
                if (annotation != null) {
                    guardInvocationCount++;
                    final Principal principal = principalProvider.get();
                    final Key<? extends Principal> callingUser;
                    final PrincipalRole callingRole;
                    if (principal != null) {
                        callingUser = principal.getPrincipalKey();
                        callingRole = principal.getUserRole();
                    } else {
                        callingUser = null;
                        callingRole = null;
                    }
                    final AuthorizationResult inspectionResult = guard.guardInvocation(invocation, annotation, callingRole, callingUser);
                    if (result == null) { // seed authorization outcome from the first result
                        result = inspectionResult;
                    } else if (inspectionResult != null && inspectionResult.success) { // allow the total outcome to be changed to success
                        result = inspectionResult;                                     // so when first authorization guard suggests failure
                                                                                       // other guards might allow the call (they are additive)
                    }
                }
            }
        }
        if (guardInvocationCount == 0) {
            return AuthorizationResult.failure("No guard was set for this method, see @AllowedFor... annotations");
        }
        return result;
    }

    public void setGuards(AuthorizationGuard[] guards) {
        if (guards != null) {
            this.guards = guards;
        } else {
            this.guards = new AuthorizationGuard[]{};
        }
    }
}
