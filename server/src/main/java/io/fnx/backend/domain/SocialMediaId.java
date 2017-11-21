package io.fnx.backend.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;
import io.fnx.backend.tools.authorization.OwnedEntity;

@Cache
@Entity
@Unindex
public class SocialMediaId implements OwnedEntity<UserEntity> {

    @Id
    private String id;

    @Index
    private Ref<UserEntity> owner;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Ref<UserEntity> getOwner() {
		return owner;
	}

	public void setOwner(Ref<UserEntity> owner) {
		this.owner = owner;
	}

	@Override
	public Key<UserEntity> getOwnerKey() {
		return getOwner().key();
	}

	public static Key<SocialMediaId> buildKey(String socialMediaId, String flavour) {
		return Key.create(SocialMediaId.class, socialMediaId+"-"+flavour);
	}
	
}
