package io.fnx.backend.domain;

import com.google.common.collect.Sets;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Set;

import static io.fnx.backend.tools.ofy.OfyUtils.nameToKey;
import static java.lang.String.format;

@Entity
@Unindex
public class FileEntity {

    public static final Set<String> IMAGE_MEDIA_TYPES = Collections.unmodifiableSet(
            Sets.newHashSet("image/jpeg", "image/png")
    );

    @Id
    private String id;

    private String name;
    private String bucketUrl;
    private String imageUrl;

    @Index
    private FileCategory category;

    @Index
    private String set; //a.k.a. folder!

    private Key<UserEntity> uploader;
    @Index
    private DateTime uploaded;
    private String mediaType;

    public static Key<FileEntity> createKey(String id) {
        return nameToKey(FileEntity.class, id);
    }

    public Key<FileEntity> getKey() {
        return createKey(id);
    }

    public void setId(String filePrefix, String fileName) {
        this.id = format("%s/%s", filePrefix, fileName);
    }

    public boolean isImage() {
        return IMAGE_MEDIA_TYPES.contains(mediaType);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBucketUrl() {
        return bucketUrl;
    }

    public void setBucketUrl(String bucketUrl) {
        this.bucketUrl = bucketUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public FileCategory getCategory() {
        return category;
    }

    public void setCategory(FileCategory category) {
        this.category = category;
    }

    public Key<UserEntity> getUploader() {
        return uploader;
    }

    public void setUploader(Key<UserEntity> uploader) {
        this.uploader = uploader;
    }

    public DateTime getUploaded() {
        return uploaded;
    }

    public void setUploaded(DateTime uploaded) {
        this.uploaded = uploaded;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}
}
