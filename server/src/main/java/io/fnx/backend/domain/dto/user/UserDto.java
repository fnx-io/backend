package io.fnx.backend.domain.dto.user;

import io.fnx.backend.domain.Role;
import io.fnx.backend.service.Validate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Validate
public class UserDto extends BaseUserDto {

    @NotBlank
    @Length(min = 6, message = "{error.password.too.short}")
    protected String password;

    protected Role role;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
