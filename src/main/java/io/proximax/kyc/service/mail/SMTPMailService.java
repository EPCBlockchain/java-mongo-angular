package io.proximax.kyc.service.mail;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.github.jhipster.config.JHipsterProperties;
import io.proximax.kyc.config.ApplicationProperties;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class SMTPMailService {

    private final Logger log = LoggerFactory.getLogger(SMTPMailService.class);
    private final JavaMailSender javaMailSender;
    private final JHipsterProperties jHipsterProperties;

    private Integer mailType = 0;
    // smtp = 0;
    // mailgun = 3;

    public SMTPMailService(JavaMailSender javaMailSender, JHipsterProperties jHipsterProperties) {
        this.javaMailSender = javaMailSender;
        this.jHipsterProperties = jHipsterProperties;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            String[] emails = to.split(",");
            message.setTo(emails);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            log.info("from {}", jHipsterProperties.getMail().getFrom(), jHipsterProperties.getMail() );
            javaMailSender.send(mimeMessage);
            log.info("Sent email to User ... '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    // @Async
    // public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
    //     Locale locale = Locale.forLanguageTag(user.getLangKey());
    //     Context context = new Context(locale);
    //     context.setVariable(USER, user);
    //     context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
    //     String content = templateEngine.process(templateName, context);
    //     String subject = messageSource.getMessage(titleKey, null, locale);
    //     sendEmail(user.getEmail(), subject, content, false, true);

    // }

    // @Async
    // public void sendActivationEmail(User user) {
    //     log.debug("Sending activation email to '{}'", user.getEmail());
    //     sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    // }

    // @Async
    // public void sendCreationEmail(User user) {
    //     log.debug("Sending creation email to '{}'", user.getEmail());
    //     sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    // }

    // @Async
    // public void sendPasswordResetMail(User user) {
    //     log.debug("Sending password reset email to '{}'", user.getEmail());
    //     sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    // }

    // @Async
    // public String sendFormRequest(String formId, String orgName, String orgId, String email) {
    //     Locale locale = Locale.forLanguageTag("en");
    //     Context context = new Context(locale);
    //     context.setVariable("orgName", orgName);
    //     context.setVariable("formId", formId);
    //     context.setVariable("orgId", orgId);
    //     context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
    //     String content = templateEngine.process("mail/formRequestEmail", context);
    //     String subject = "Form Request";
    //     sendEmail(email, subject, content, false, true);
    //     return "OK";
    // }

    // @Async
    // public String sendFormResubmitRequest(String submissionId, String orgName, String email, String note) {
    //     Locale locale = Locale.forLanguageTag("en");
    //     Context context = new Context(locale);
    //     context.setVariable("orgName", orgName);
    //     context.setVariable("submissionId", submissionId);
    //     context.setVariable("note", note);
    //     context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
    //     String content = templateEngine.process("mail/formRequestEditEmail", context);
    //     String subject = orgName + " requires additional information";
    //     sendEmail(email, subject, content, false, true);
    //     return "OK";
    // }

    // @Async
    // public void sendRejectionEmail(User user, String id) {

    //     Locale locale = Locale.forLanguageTag("en");
    //     Context context = new Context(locale);
    //     context.setVariable("userFormId", id);
    //     context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
    //     String content = templateEngine.process("mail/rejectionEmail", context);
    //     String subject = "Form Request";
    //     sendEmail(user.getEmail(), subject, content, false, true);

    // }

    // @Async
    // public void sendApprovalEmail(User user, String id) {

    //     Locale locale = Locale.forLanguageTag("en");
    //     Context context = new Context(locale);
    //     context.setVariable("userFormId", id);
    //     context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
    //     String content = templateEngine.process("mail/approvalEmail", context);
    //     String subject = "Form Request";
    //     sendEmail(user.getEmail(), subject, content, false, true);

    // }

}

