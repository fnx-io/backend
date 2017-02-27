package io.fnx.backend.service;

import io.fnx.backend.domain.FileEntity;
import io.fnx.backend.service.filter.ListFilesFilter;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public interface FileService {

    FileEntity storeFile(String fileName, String set, MediaType mediaType, InputStream inputStream);

    ListResult<FileEntity> listFiles(ListFilesFilter filter);
}
