package io.fnx.backend.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import org.joda.time.DateTime;

import java.util.*;

import static io.fnx.backend.tools.ofy.OfyUtils.idToKey;

@Entity
@Unindex
public class CmsArticleEntity {

	@Id
	private Long id;

	private String name;

	@Index
	private String type;

	@Index
	private DateTime created;

	private Key<UserEntity> createdBy;

	@Ignore
	private String authorName;

	private Map<String, Object> data;

	public static Key<CmsArticleEntity> createKey(Long id) {
		return idToKey(CmsArticleEntity.class, id);
	}

	public Key<CmsArticleEntity> getKey() {
		return createKey(id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public Key<UserEntity> getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Key<UserEntity> createdBy) {
		this.createdBy = createdBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getData() {
		if (data == null) data = new HashMap<String, Object>();
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

}
