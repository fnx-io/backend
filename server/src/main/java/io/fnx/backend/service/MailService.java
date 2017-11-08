package io.fnx.backend.service;

import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.domain.dto.login.LoginResult;
import io.fnx.backend.domain.dto.user.UpdateUserDto;
import io.fnx.backend.domain.dto.user.UserDto;
import io.fnx.backend.domain.filter.user.ListUsersFilter;

import java.util.Map;

/**
 * Use it to send emails!
 */
public interface MailService {

	void sendEmail(UserEntity to, String subject, String htmlBody);

	void sendEmail(UserEntity to, String subject, String templateName, Map<String, Object> templateParams);

}
