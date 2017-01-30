package com.backend.service;

import com.backend.domain.FileEntity;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public interface FileService {

    FileEntity storeFile(String fileName, MediaType mediaType, InputStream inputStream);
}
