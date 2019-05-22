package io.fnx.backend.service;

import io.fnx.backend.domain.CmsArticleEntity;
import io.fnx.backend.service.filter.CmsArticleFilter;

public interface CmsArticleService {

    CmsArticleEntity createArticle(CmsArticleEntity cms);

    CmsArticleEntity updateArticle(CmsArticleEntity cms);

    ListResult<CmsArticleEntity> listArticles(CmsArticleFilter filter);

	CmsArticleEntity findById(Long id);
}
