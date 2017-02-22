package io.fnx.backend.domain;

import com.google.common.collect.Sets;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.fnx.backend.tools.ofy.OfyUtils.idToKey;
import static io.fnx.backend.tools.ofy.OfyUtils.nameToKey;
import static java.lang.String.format;

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
}
