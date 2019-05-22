package io.fnx.backend.service;

import io.fnx.backend.domain.UserEntity;

import java.util.Map;

/**
 * Use it to send emails!
 */
public interface MailService {

	void sendEmail(UserEntity to, String subject, String htmlBody);

	void sendEmail(UserEntity to, String subject, String templateName, Map<String, Object> templateParams);

}
