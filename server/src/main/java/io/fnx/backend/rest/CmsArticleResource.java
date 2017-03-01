package io.fnx.backend.rest;

import com.googlecode.objectify.Key;
import io.fnx.backend.auth.CallContext;
import com.googlecode.objectify.Key;
import io.fnx.backend.domain.CmsArticleEntity;
import io.fnx.backend.domain.Role;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.domain.eventlog.AuditLogEventEntity;
import io.fnx.backend.service.AuditLogManager;
import io.fnx.backend.service.CmsArticleService;
import io.fnx.backend.service.ListResult;
import io.fnx.backend.service.filter.AuditLogEventFilter;
import io.fnx.backend.service.filter.CmsArticleFilter;
import io.fnx.backend.tools.hydration.HydrationRecipe;
import io.fnx.backend.tools.hydration.HydrationRecipeStep;
import io.fnx.backend.tools.hydration.SimpleHydrationRecipe;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.*;

/**
 * REST endpoints for articles.
 */
@Path("/v1/cms/articles")
public class CmsArticleResource extends BaseResource {

    private CmsArticleService articleService;

	private AuditLogManager auditLogManager;

    @POST
    public CmsArticleEntity createArticle(CmsArticleEntity articleEntity) {
        CmsArticleEntity result = articleService.createArticle(articleEntity);
        hydrator.hydrateEntity(result, RECIPE, cc());
        return result;
    }

    @PUT
    @Path("/{id}")
    public CmsArticleEntity update(@PathParam("id") Long id, CmsArticleEntity articleEntity) {
        articleEntity.setId(id);
	    CmsArticleEntity result = articleService.updateArticle(articleEntity);
	    hydrator.hydrateEntity(result, RECIPE, cc());
	    return result;
    }

	@GET
	@Path("/{id}")
	public CmsArticleEntity findById(@PathParam("id") Long id) {
		CmsArticleEntity result = articleService.findById(id);
		hydrator.hydrateEntity(result, RECIPE, cc());
		return result;

	}
	
	@GET
	public ListResult<CmsArticleEntity> list(@QueryParam("type") String type) {
		ListResult<CmsArticleEntity> result = articleService.listArticles(new CmsArticleFilter(type, filterLimits()));
		hydrator.hydrateCollection(result, RECIPE, cc());
		return result;
	}

	@GET
	@Path("/{id}/log")
	public ListResult<AuditLogEventEntity> list(@PathParam("id") Long id) {
    	Key articleKey = CmsArticleEntity.createKey(id);
		return auditLogManager.listAuditLogEvents(new AuditLogEventFilter(articleKey, filterLimits()));
	}

	@Inject
	public void setArticleService(CmsArticleService articleService) {
		this.articleService = articleService;
	}

	@Inject
	public void setAuditLogManager(AuditLogManager auditLogManager) {
		this.auditLogManager = auditLogManager;
	}

}

	static HydrationRecipe<CmsArticleEntity, CallContext> RECIPE = new SimpleHydrationRecipe<>(
			new ArticleHydrationStep(),
			new ExamplePrivacyStep()
	);

	private static class ArticleHydrationStep implements HydrationRecipeStep<CmsArticleEntity, CallContext> {

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

	private static class ExamplePrivacyStep implements HydrationRecipeStep<CmsArticleEntity, CallContext> {

		@Override
		public List<Key<?>> getDependencies(CmsArticleEntity articleEntity, CallContext callContext) {
			// no dependencies needed
			return null;
		}

		@Override
		public void executeStep(CmsArticleEntity articleEntity, CallContext callContext, Map<Key<Object>, Object> map) {
			if (callContext.getCurrentRole() != Role.ADMIN) {
				// articleEntity.sensitiveInfo = null
			}
		}
	}

}

