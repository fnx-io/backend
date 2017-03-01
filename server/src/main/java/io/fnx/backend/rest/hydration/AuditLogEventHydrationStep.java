package io.fnx.backend.rest.hydration;

import com.googlecode.objectify.Key;
import io.fnx.backend.auth.CallContext;
import io.fnx.backend.domain.CmsArticleEntity;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.tools.hydration.HydrationRecipeStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleHydrationStep implements HydrationRecipeStep<CmsArticleEntity, CallContext> {

    @Override
    public List<Key<?>> getDependencies(CmsArticleEntity articleEntity, CallContext callContext) {
        List<Key<?>> keys = new ArrayList<>();
        keys.add(articleEntity.getCreatedBy());
        return keys;

    }

    @Override
    public void executeStep(CmsArticleEntity articleEntity, CallContext callContext, Map<Key<Object>, Object> map) {
        UserEntity author = (UserEntity) map.get(articleEntity.getCreatedBy());
        articleEntity.setAuthorName(author.getName());
    }
}
