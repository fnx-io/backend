package com.backend.domain.dto.user;

import com.backend.service.Validate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Validate
public class UserDto extends BaseUserDto {

    @NotBlank
    @Length(min = 6, message = "{error.password.too.short}")
    protected String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
