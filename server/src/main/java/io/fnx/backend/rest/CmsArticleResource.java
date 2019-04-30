package io.fnx.backend.rest;

import com.googlecode.objectify.Key;
import io.fnx.backend.auth.CallContext;
import io.fnx.backend.domain.CmsArticleEntity;
import io.fnx.backend.domain.eventlog.AuditLogEventEntity;
import io.fnx.backend.rest.hydration.ArticleHydrationStep;
import io.fnx.backend.rest.hydration.AuditLogEventHydrationStep;
import io.fnx.backend.rest.hydration.ExamplePrivacyStep;
import io.fnx.backend.service.AuditLogManager;
import io.fnx.backend.service.CmsArticleService;
import io.fnx.backend.service.ListResult;
import io.fnx.backend.service.filter.AuditLogEventFilter;
import io.fnx.backend.service.filter.CmsArticleFilter;
import io.fnx.backend.tools.authorization.AllowedForAdmins;
import io.fnx.backend.tools.authorization.AllowedForAuthenticated;
import io.fnx.backend.tools.hydration.HydrationRecipe;
import io.fnx.backend.tools.hydration.SimpleHydrationRecipe;

import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * REST endpoints for articles.
 */
@Path("/v1/cms/articles")
public class CmsArticleResource extends BaseResource {

    private CmsArticleService articleService;

    private AuditLogManager auditLogManager;

    @POST
    @AllowedForAdmins
    public CmsArticleEntity createArticle(CmsArticleEntity articleEntity) {
        CmsArticleEntity result = articleService.createArticle(articleEntity);
        hydrator.hydrateEntity(result, ARTICLE_RECIPE, cc());
        return result;
    }

    @PUT
    @Path("/{id}")
    @AllowedForAdmins
    public CmsArticleEntity update(@PathParam("id") Long id, CmsArticleEntity articleEntity) {
        articleEntity.setId(id);
        CmsArticleEntity result = articleService.updateArticle(articleEntity);
        hydrator.hydrateEntity(result, ARTICLE_RECIPE, cc());
        return result;
    }

    @GET
    @Path("/{id}")
    @AllowedForAuthenticated
    public CmsArticleEntity findById(@PathParam("id") Long id) {
        CmsArticleEntity result = articleService.findById(id);
        hydrator.hydrateEntity(result, ARTICLE_RECIPE, cc());
        return result;

    }

    @GET
    @AllowedForAuthenticated
    public ListResult<CmsArticleEntity> list(@QueryParam("type") String type) {
        ListResult<CmsArticleEntity> result = articleService.listArticles(new CmsArticleFilter(type, filterLimits()));
        hydrator.hydrateCollection(result, ARTICLE_RECIPE, cc());
        return result;
    }

    @GET
    @Path("/{id}/log")
    @AllowedForAuthenticated
    public ListResult<AuditLogEventEntity> listLogEvents(@PathParam("id") Long id) {
        Key articleKey = CmsArticleEntity.createKey(id);
        ListResult<AuditLogEventEntity>  result = auditLogManager.listAuditLogEvents(new AuditLogEventFilter(articleKey, filterLimits()));
        hydrator.hydrateCollection(result, EVENT_RECIPE, cc());
        return result;
    }

    @Inject
    public void setArticleService(CmsArticleService articleService) {
        this.articleService = articleService;
    }

    @Inject
    public void setAuditLogManager(AuditLogManager auditLogManager) {
        this.auditLogManager = auditLogManager;
    }

    static HydrationRecipe<CmsArticleEntity, CallContext> ARTICLE_RECIPE = new SimpleHydrationRecipe<>(
            new ArticleHydrationStep(),
            new ExamplePrivacyStep()
    );

    static HydrationRecipe<AuditLogEventEntity, CallContext> EVENT_RECIPE = new SimpleHydrationRecipe<>(new AuditLogEventHydrationStep());

}

