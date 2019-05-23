package io.fnx.backend.auth.guards;

import io.fnx.backend.tools.auth.PrincipalRole;
import io.fnx.backend.tools.authorization.AllowedForRolesAuthorizationGuard;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class AllowedForRolesAuthorizationGuardImpl extends AllowedForRolesAuthorizationGuard<AllowedForRoles> {

    public AllowedForRolesAuthorizationGuardImpl() {
        super(AllowedForRoles.class);
    }

    @Override
    public Collection<PrincipalRole> getRoles(AllowedForRoles annotation) {
        return annotation != null ? Arrays.asList(annotation.value()) : Collections.emptyList();
    }
}
