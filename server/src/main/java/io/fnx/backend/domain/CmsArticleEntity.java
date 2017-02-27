package io.fnx.backend.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import io.fnx.backend.tools.hydration.*;
import org.joda.time.DateTime;

import java.util.*;

import static io.fnx.backend.tools.ofy.OfyUtils.idToKey;
import static java.lang.String.format;

@Entity
@Unindex
public class CmsArticleEntity implements CanBeHydrated<CmsArticleEntity, HydrationContext> {

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

	@Override
	public HydrationRecipe getRecipe() {
		return new CmsArticleHydrationRecipe(this);
	}
}

class CmsArticleHydrationRecipe implements HydrationRecipe {

	private CmsArticleEntity articleEntity;

	public CmsArticleHydrationRecipe(CmsArticleEntity articleEntity) {
		this.articleEntity = articleEntity;
	}

	@Override
	public Object transformForApi(HydrationContext ctx) {
		return articleEntity;
	}

	@Override
	public Collection<HydratedProperty> propsToHydrate(HydrationContext ctx) {
		HydratedProperty createdByInfo = new SingleValueHydratedProperty<CmsArticleEntity, UserEntity>() {
			@Override
			public Key<UserEntity> getKey(CmsArticleEntity object) {
				return object.getCreatedBy();
			}

			@Override
			public void setProperty(CmsArticleEntity object, UserEntity entity) {
				object.setAuthorName(entity.getName());
			}
		};
		return Arrays.asList(createdByInfo);
	}
};

