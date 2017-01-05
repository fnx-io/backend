package com.backend.domain.dto;

import com.backend.service.Validate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Validate
public class UserDto {

    @Email
    @NotBlank
    protected String email;

    @NotBlank
    protected String name;

    @NotBlank
    @Length(min = 6, message = "{error.password.too.short}")
    protected String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
