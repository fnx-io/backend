package io.fnx.backend.rest;

import com.google.appengine.api.images.*;
import io.fnx.backend.domain.FileCategory;
import io.fnx.backend.domain.FileEntity;
import io.fnx.backend.service.FileService;
import io.fnx.backend.service.ListResult;
import io.fnx.backend.service.filter.ListFilesFilter;
import io.fnx.backend.tools.authorization.AllowedForAuthenticated;
import io.fnx.backend.tools.random.Randomizer;
import io.fnx.backend.util.Constants;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.io.ByteStreams.copy;

@Path("/v1/files")
public class FileResource extends BaseResource {

	private Randomizer randomizer;
	private FileService fileService;

	@POST
	@Consumes(MediaType.WILDCARD)
	@AllowedForAuthenticated
	public Response createFile(@Context HttpHeaders headers, @Context Request request, @QueryParam("set") String set, InputStream data) {
		final String fileName = getFileName(headers.getRequestHeader(Constants.HEADER_FILENAME));
		final MediaType mediaType = headers.getMediaType();

		return created(fileService.storeFile(fileName, set, mediaType, data));
	}

	@POST
	@Path("/image")
	@Consumes("image/*")
	@AllowedForAuthenticated
	public Response createImageFile(@Context HttpHeaders headers, @Context Request request, InputStream data,
	                                @QueryParam("set") String set,
	                                @QueryParam("x") double x, @QueryParam("y") double y,
	                                @QueryParam("widthRatio") double widthRatio, @QueryParam("heightRatio") double heightRatio) throws IOException {

		ByteArrayOutputStream boos = new ByteArrayOutputStream();
		copy(data, boos);
		data.close();
		boos.flush();
		byte[] image = boos.toByteArray();
		boos = null;

		// all images in TravelFriends are JPGs
		byte[] cropped = crop(image, x, y, widthRatio, heightRatio);
		image = null;

		final String fileName = getFileName(headers.getRequestHeader(Constants.HEADER_FILENAME));
		final MediaType mediaType = new MediaType("image", "jpeg");

		return created(fileService.storeFile(fileName, set, mediaType, new ByteArrayInputStream(cropped)));
	}

	private byte[] crop(byte[] image, double x, double y, double widthRatio, double heightRatio) {
		// no crop if the ratio is not set
		if (widthRatio == 0 || heightRatio == 0) return image;
		final Image img = ImagesServiceFactory.makeImage(image);
		final Transform cropTransform = ImagesServiceFactory.makeCrop(x, y, x+widthRatio, y+heightRatio);

		// enforcing JPG
		OutputSettings outputSettings = new OutputSettings(ImagesService.OutputEncoding.JPEG);
		outputSettings.setQuality(85);

		final Image cropped = ImagesServiceFactory.getImagesService().applyTransform(cropTransform, img, outputSettings);
		return cropped.getImageData();
	}

	@GET
	@AllowedForAuthenticated
	public ListResult<FileEntity> list(@QueryParam("category") FileCategory category, @QueryParam("set") String set) {
		return fileService.listFiles(new ListFilesFilter(category, set, filterLimits()));
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
