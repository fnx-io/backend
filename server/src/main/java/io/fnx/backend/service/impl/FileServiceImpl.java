package io.fnx.backend.service.impl;

import io.fnx.backend.domain.FileCategory;
import io.fnx.backend.domain.FileEntity;
import io.fnx.backend.service.BaseService;
import io.fnx.backend.service.FileService;
import io.fnx.backend.service.ListResult;
import io.fnx.backend.service.filter.ListFilesFilter;
import io.fnx.backend.util.conf.AppConfiguration;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.*;
import com.google.common.base.Joiner;
import com.googlecode.objectify.cmd.Query;
import io.fnx.backend.tools.authorization.AllowedForAdmins;
import io.fnx.backend.tools.authorization.AllowedForAuthenticated;
import io.fnx.backend.tools.random.Randomizer;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.io.ByteStreams.copy;
import static java.lang.String.format;

public class FileServiceImpl extends BaseService implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private static final String PUBLIC_URL = "https://storage.googleapis.com/{bucket}/{fileId}";

    private Randomizer randomizer;

    private String fileBucket;

    @Override
    @AllowedForAuthenticated
    public FileEntity storeFile(String fileName, String set, MediaType mediaType, InputStream inputStream) {
        checkArgument(!isNullOrEmpty(fileName), "Missing file name");
        checkNotNull(inputStream, "Missing file data");
        if (mediaType == null) mediaType = MediaType.APPLICATION_OCTET_STREAM_TYPE;

        final String filePrefix = randomizer.randomBase64(20);
        final FileEntity f = new FileEntity();
        f.setId(filePrefix, fileName);
        f.setName(fileName);
        f.setUploader(cc().getLoggedUser().getKey());
        f.setUploaded(DateTime.now());
        f.setMediaType(mediaType.toString());
        if (f.isImage()) {
            f.setCategory(FileCategory.IMAGE);
	        checkArgument(!isNullOrEmpty(set), "When file is an image, you must specify 'set'");
	        f.setSet(set);
        } else {
            f.setCategory(FileCategory.OTHER);
	        f.setSet(set); // set optional ... ?
        }

        final GcsFilename gcsFilename = storeInBucket(fileBucket, f.getId(), f.getMediaType(), inputStream);
        String imageUrl = null;
        if (f.getCategory() == FileCategory.IMAGE) {
            imageUrl = storeAsImage(gcsFilename);
        }
        f.setBucketUrl(renderPublicBucketUrl(gcsFilename));
        f.setImageUrl(imageUrl);

        ofy().save().entity(f).now();

        return f;
    }

    @Override
    @AllowedForAdmins
    public ListResult<FileEntity> listFiles(ListFilesFilter filter) {
        final Query<FileEntity> query = ofy().load().type(FileEntity.class);
        final List<FileEntity> result = filter.query(query).list();
        return filter.result(result);
    }

    public static String renderPublicBucketUrl(GcsFilename gcsFilename) {
        return UriBuilder.fromPath(PUBLIC_URL).build(gcsFilename.getBucketName(), gcsFilename.getObjectName()).toString();
    }

    private String storeAsImage(GcsFilename gcsFilename) {
        if (gcsFilename == null) return null;
        log.info(format("Storing file %s/%s in image service", gcsFilename.getBucketName(), gcsFilename.getObjectName()));
        final String gcsPath = format("/gs/%s/%s", gcsFilename.getBucketName(), gcsFilename.getObjectName());
        final ServingUrlOptions options = ServingUrlOptions.Builder.withGoogleStorageFileName(gcsPath);

        return ImagesServiceFactory.getImagesService().getServingUrl(options);
    }

    private GcsFilename storeInBucket(String bucketName, String id, String mimeType, InputStream inputStream) {
        final GcsFilename gcsFile = getGcsObjectName(bucketName, id);
        final GcsFileOptions opts = new GcsFileOptions.Builder().mimeType(mimeType).acl("public-read").build();

        try {
            log.info(format("Start storing file in %s/%s", gcsFile.getBucketName(), gcsFile.getObjectName()));
            final GcsService service = GcsServiceFactory.createGcsService();
            GcsOutputChannel outputChannel = service.createOrReplace(gcsFile, opts);
            copy(inputStream, Channels.newOutputStream(outputChannel));
            outputChannel.waitForOutstandingWrites();
            outputChannel.close();
            log.info(format("Finished storing file in %s/%s", gcsFile.getBucketName(), gcsFile.getObjectName()));
        } catch (IOException e) {
            log.info(format("Error while storing file in %s/%s", gcsFile.getBucketName(), gcsFile.getObjectName()));
            throw new IllegalStateException("Bucket write failed", e);
        }
        return gcsFile;
    }

    private GcsFilename getGcsObjectName(String bucketName, String id) {
        checkArgument(!isNullOrEmpty(bucketName), "Bucket to store files into might not be null!");
        checkArgument(!isNullOrEmpty(id));

        // use first 2 letters as directories
        // a/bcdef => a/b/a/bcdef
        List<String> prefixes = new ArrayList<>();
        for (char c : id.toCharArray()) {
            if (prefixes.size() == 2) break;
            if (Character.isLetter(c) || Character.isDigit(c)) prefixes.add(Character.toString(c));
        }
        final String directoryPath = Joiner.on("/").join(prefixes);
        final String objectName;
        if (directoryPath.isEmpty()) {
            objectName = id;
        } else {
            objectName = format("%s/%s", directoryPath, id);
        }

        return new GcsFilename(bucketName, objectName);
    }

    @Inject
    public void setRandomizer(Randomizer randomizer) {
        this.randomizer = randomizer;
    }

    @Inject
    public void setConfiguration(AppConfiguration cfg) {
        this.fileBucket = cfg.getProperty("file.bucket");
    }
}
