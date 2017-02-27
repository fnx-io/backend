package io.fnx.backend.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import io.fnx.backend.tools.auth.Principal;
import io.fnx.backend.tools.auth.PrincipalRole;
import io.fnx.backend.tools.authorization.OwnedEntity;

import static io.fnx.backend.tools.ofy.OfyUtils.idToKey;

@Cache
@Entity
@Unindex
public class UserEntity implements Principal, OwnedEntity<UserEntity> {

    @Id
    private Long id;
    @Index
    private String email;
    @Index
    private String name;
    private Role role;
    @JsonIgnore
    private String passwordHash;


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
    public PrincipalRole getUserRole() {
        return role;
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
                ", name='" + name + '\'' +
                ", role=" + role +
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
	
}
