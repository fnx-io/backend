package io.fnx.backend.domain.dto.user;

import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.service.Validate;
import com.googlecode.objectify.Key;
import io.fnx.backend.tools.authorization.OwnedEntity;
import org.hibernate.validator.constraints.Length;

@Validate
public class UpdateUserDto extends BaseUserDto implements OwnedEntity<UserEntity> {

    protected Long userId;

    @Length(min = 6, message = "{error.password.too.short}")
    protected String password;

    @Override
    public Key<UserEntity> getOwnerKey() {
        return UserEntity.createKey(userId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
