package io.fnx.backend.domain.dto.user;

import io.fnx.backend.service.Validate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Validate
public class PasswordChangeDto {

	@Email
	@NotBlank
	protected String email;

    @NotBlank
    @Length(min = 6, message = "{error.password.too.short}")
    protected String password;

    @NotBlank
	protected String token;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
