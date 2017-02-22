package io.fnx.backend.service;

import io.fnx.backend.domain.CmsArticleEntity;
import io.fnx.backend.domain.FileEntity;
import io.fnx.backend.service.filter.CmsArticleFilter;
import io.fnx.backend.service.filter.ListFilesFilter;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public interface CmsArticleService {

    CmsArticleEntity createArticle(CmsArticleEntity cms);

    CmsArticleEntity updateArticle(CmsArticleEntity cms);

    ListResult<CmsArticleEntity> listArticles(CmsArticleFilter filter);

	CmsArticleEntity findById(Long id);
}
