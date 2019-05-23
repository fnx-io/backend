package io.fnx.backend.rest.hydration;

import com.googlecode.objectify.Key;
import io.fnx.backend.auth.CallContext;
import io.fnx.backend.domain.CmsArticleEntity;
import io.fnx.backend.domain.Role;
import io.fnx.backend.tools.hydration.HydrationRecipeStep;

import java.util.List;
import java.util.Map;

public class ExamplePrivacyStep implements HydrationRecipeStep<CmsArticleEntity, CallContext> {

    @Override
    public List<Key<?>> getDependencies(CmsArticleEntity articleEntity, CallContext callContext) {
        // no dependencies needed
        return null;
    }

    @Override
    public void executeStep(CmsArticleEntity articleEntity, CallContext callContext, Map<Key<Object>, Object> map) {
        if (!callContext.isAdmin()) {
            // articleEntity.sensitiveInfo = null
        }
    }
}
