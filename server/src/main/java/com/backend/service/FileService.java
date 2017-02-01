package com.backend.service;

import com.backend.domain.FileEntity;
import com.backend.service.filter.ListFilesFilter;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public interface FileService {

    FileEntity storeFile(String fileName, MediaType mediaType, InputStream inputStream);

    ListResult<FileEntity> listFiles(ListFilesFilter filter);
}
