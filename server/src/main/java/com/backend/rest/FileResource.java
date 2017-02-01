package com.backend.rest;

import com.backend.domain.FileCategory;
import com.backend.domain.FileEntity;
import com.backend.service.FileService;
import com.backend.service.ListResult;
import com.backend.service.filter.ListFilesFilter;
import com.backend.util.Constants;
import io.fnx.backend.tools.random.Randomizer;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

@Path("/v1/files")
public class FileResource extends BaseResource {

    private Randomizer randomizer;
    private FileService fileService;

    @POST
    @Consumes(MediaType.WILDCARD)
    public Response createFile(@Context HttpHeaders headers, @Context Request request, InputStream data) throws IOException {
        final String fileName = getFileName(headers.getRequestHeader(Constants.HEADER_FILENAME));
        final MediaType mediaType = headers.getMediaType();

        return created(fileService.storeFile(fileName, mediaType, data));
    }

    @GET
    public ListResult<FileEntity> list(@QueryParam("category") FileCategory category) {
        return fileService.listFiles(new ListFilesFilter(category, filterLimits()));
    }

    private String getFileName(List<String> filenameHeaders) {
        if (filenameHeaders == null || filenameHeaders.isEmpty() || isNullOrEmpty(filenameHeaders.get(0))) {
            return randomizer.randomBase64(10);
        } else {
            return filenameHeaders.get(0);
        }
    }

    @Inject
    public void setRandomizer(Randomizer randomizer) {
        this.randomizer = randomizer;
    }

    @Inject
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
