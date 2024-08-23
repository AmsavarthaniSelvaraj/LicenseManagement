package com.example.LicenseManagement.emailconfig;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class mailConfig {

	@Value("${mail.smtp.host}")
	private String smtpHost;

	@Value("${mail.smtp.port}")
	private int smtpPort;

	@Value("${mail.smtp.username}")
	private String username;

	@Value("${mail.smtp.password}")
	private String password;

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl sendingMail = new JavaMailSenderImpl();
		sendingMail.setHost(smtpHost);
		sendingMail.setPort(smtpPort);
		sendingMail.setUsername(username);
		sendingMail.setPassword(password);

		Properties props = sendingMail.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return getJavaMailSender();

	}

}
