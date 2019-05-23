package io.fnx.backend.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import io.fnx.backend.tools.auth.Principal;
import io.fnx.backend.tools.auth.PrincipalRole;
import io.fnx.backend.tools.authorization.OwnedEntity;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import static io.fnx.backend.tools.ofy.OfyUtils.idToKey;

@Cache
@Entity
@Unindex
public class UserEntity implements Principal, OwnedEntity<UserEntity> {

    @Id
    private Long id;

    @Index
    private String email;

    private String firstName;

	@Index
	private String lastName;

    private List<Role> roles;

    @JsonIgnore
    private String passwordHash;
	
	@JsonIgnore
    private DateTime passwordTokenValidTill;

	@JsonIgnore
    private String passwordToken;
	
	@Index
	private String avatarUrl;
	
    public static Key<UserEntity> createKey(Long id) {
        return idToKey(UserEntity.class, id);
    }

    public Key<UserEntity> getKey() {
        return createKey(id);
    }

    @Override
    public Key<? extends Principal> getPrincipalKey() {
        return getKey();
    }

    @Override
    public List<? extends PrincipalRole> getUserRoles() {
        return roles;
    }

    @Override
    public Key<UserEntity> getOwnerKey() {
        return getKey(); //users "own" themselves
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;

        UserEntity that = (UserEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
    	if (lastName == null) {
    		if (firstName == null) return "";
    		return firstName;
	    }
	    if (firstName == null) {
    		return lastName;
	    }
	    return firstName+" "+lastName;
    }

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public DateTime getPasswordTokenValidTill() {
		return passwordTokenValidTill;
	}

	public void setPasswordTokenValidTill(DateTime passwordTokenValidTill) {
		this.passwordTokenValidTill = passwordTokenValidTill;
	}

	public String getPasswordToken() {
		return passwordToken;
	}

	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}
	
	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setSingleRole(Role role) {
        this.roles = Collections.singletonList(role);
    }
}
