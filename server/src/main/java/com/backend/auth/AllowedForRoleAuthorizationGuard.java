package com.backend.auth;

import com.google.common.collect.Lists;
import io.fnx.backend.tools.auth.PrincipalRole;
import io.fnx.backend.tools.authorization.AllowedForRolesAuthorizationGuard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AllowedForRoleAuthorizationGuard extends AllowedForRolesAuthorizationGuard<AllowedForRoles> {

    public AllowedForRoleAuthorizationGuard() {
        super(AllowedForRoles.class);
    }

    @Override
    public Collection<PrincipalRole> getRoles(AllowedForRoles annotation) {
        if (annotation == null) return Lists.newArrayList();
        final ArrayList<PrincipalRole> ret = Lists.newArrayListWithCapacity(annotation.value().length);
        Collections.addAll(ret, annotation.value());
        return ret;
    }
}
