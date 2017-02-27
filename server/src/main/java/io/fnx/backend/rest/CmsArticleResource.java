package io.fnx.backend.rest;

import com.googlecode.objectify.Key;
import io.fnx.backend.domain.CmsArticleEntity;
import io.fnx.backend.domain.eventlog.AuditLogEventEntity;
import io.fnx.backend.service.AuditLogManager;
import io.fnx.backend.service.CmsArticleService;
import io.fnx.backend.service.ListResult;
import io.fnx.backend.service.filter.AuditLogEventFilter;
import io.fnx.backend.service.filter.CmsArticleFilter;

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
    public CmsArticleEntity createArticle(CmsArticleEntity articleEntity) {
        return articleService.createArticle(articleEntity);
    }

    @PUT
    @Path("/{id}")
    public CmsArticleEntity update(@PathParam("id") Long id, CmsArticleEntity articleEntity) {
        articleEntity.setId(id);
        return articleService.updateArticle(articleEntity);
    }

	@GET
	@Path("/{id}")
	public CmsArticleEntity findById(@PathParam("id") Long id) {
		return articleService.findById(id);
	}
	
	@GET
	public ListResult<CmsArticleEntity> list(@QueryParam("type") String type) {
		return articleService.listArticles(new CmsArticleFilter(type, filterLimits()));
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
