package io.fnx.backend.service.impl;

import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;
import io.fnx.backend.domain.CmsArticleEntity;
import io.fnx.backend.domain.FileEntity;
import io.fnx.backend.service.BaseService;
import io.fnx.backend.service.CmsArticleService;
import io.fnx.backend.service.ListResult;
import io.fnx.backend.service.filter.CmsArticleFilter;
import io.fnx.backend.tools.auth.Principal;
import io.fnx.backend.tools.authorization.AllowedForAdmins;
import io.fnx.backend.tools.hydration.HydrationContext;
import io.fnx.backend.tools.hydration.Hydrator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class CmsArticleServiceImpl extends BaseService implements CmsArticleService {

    private static final Logger log = LoggerFactory.getLogger(CmsArticleServiceImpl.class);

	@Override
	@AllowedForAdmins
	public CmsArticleEntity createArticle(CmsArticleEntity articleEntity) {
		checkArgument(articleEntity!=null, "Missing article");
		checkArgument(articleEntity.getType()!=null, "Missing article type");

		articleEntity.setCreatedBy(cc().getLoggedUser().getKey());
		articleEntity.setCreated(DateTime.now());

		ofy().save().entity(articleEntity).now();

		return articleEntity;
	}

	@Override
	// AllowedForAll
	public CmsArticleEntity findById(Long id) {
		return ofy().load().key(CmsArticleEntity.createKey(id)).now();
	}

	@Override
	@AllowedForAdmins
	public CmsArticleEntity updateArticle(final CmsArticleEntity articleEntity) {
		checkArgument(articleEntity!=null, "Missing article");
		checkArgument(articleEntity.getId()!=null, "Missing article id");
		checkArgument(articleEntity.getType()!=null, "Missing article type");

		return ofy().transact(new Work<CmsArticleEntity>() {
			@Override
			public CmsArticleEntity run() {
				CmsArticleEntity persistent = ofy().load().key(articleEntity.getKey()).now();
				if (!articleEntity.getType().equals(persistent.getType())) {
					throw new IllegalStateException(String.format("Changing article type is not allowed. Expected %s, found %s.", persistent.getType(), articleEntity.getType()));
				}
				// keep unmodifiable data
				articleEntity.setCreatedBy(persistent.getCreatedBy());
				articleEntity.setCreated(persistent.getCreated());
				ofy().save().entity(articleEntity).now();
				return articleEntity;
			}
		});
	}

	@Override
	// AllowedForAll
	public ListResult<CmsArticleEntity> listArticles(CmsArticleFilter filter) {
		final Query<CmsArticleEntity> query = ofy().load().type(CmsArticleEntity.class);
		final List<CmsArticleEntity> result = filter.query(query).list();
		return filter.result(result);
	}

}
