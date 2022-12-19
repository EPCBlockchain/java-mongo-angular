package io.proximax.kyc.service.mail;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Locale;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import io.github.jhipster.config.JHipsterProperties;
import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.domain.mongo.ScreeningSubmission;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailGunService {

    private final Logger log = LoggerFactory.getLogger(MailGunService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final ApplicationProperties appConfig;

    private final JHipsterProperties jHipsterProperties;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailGunService(JHipsterProperties jHipsterProperties,ApplicationProperties appConfig, RestTemplateBuilder builder,
            MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.appConfig = appConfig;
        this.jHipsterProperties = jHipsterProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);
        try {

            String from = jHipsterProperties.getMail().getFrom();
            MultiValueMap<String, String> map = getPostRequestObject(from, to, subject);
            map.add("html", content);
            try {
                Unirest.post(appConfig.getMailgun().getMessageurl())
                    .basicAuth("api", appConfig.getMailgun().getApiKey())
                    .field("from", from)
                    .field("to", to)
                    .field("subject", subject)
                    .field("html", content)
                    .asJson();
            } catch (UnirestException ex) {
                log.debug("UnirestException : ", ex.getMessage());
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    // private void sendEmail(MultiValueMap<String, String> map) {
    //     String messagesUrl = appConfig.getMailgun().getMessageurl();
    //     String username = appConfig.getMailgun().getUsername();
    //     String password = appConfig.getMailgun().getPassword();
    //     this.post(messagesUrl, map, username, password);
    // }

    private MultiValueMap<String, String> getPostRequestObject(String from, String to, String subject) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("from", from);
        map.add("to", to);
        map.add("subject", subject);
        return map;
    }
    // public void post(String resourceUrl, MultiValueMap<String, String> request, String username, String password) {
    //     HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(request,
    //             createHeadersWithUsernamePassword(username, password));
    // }

    private HttpHeaders createHeadersWithUsernamePassword(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "api " + appConfig.getMailgun().getApiKey();//new String(encodedAuth);
                set("Accept", MediaType.APPLICATION_JSON.toString());
                set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
                set("Authorization", authHeader);
            }
        };
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        log.debug(content);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendStatusEmaill(String emails, ScreeningSubmission screeningSubmission) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("screeningSubmissionId", screeningSubmission.getId());
        context.setVariable("status", screeningSubmission.getStatus());
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("mail/statusEmail", context);
        String subject = "Submission Update";
        sendEmail(emails, subject, content, false, true);
    }

    @Async
    public void sendSubmissionReceivedEmail(String emails, KYCSubmission kycSubmission) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("kycSubmissionId", kycSubmission.getId());
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("mail/sendSubmissionReceivedEmail", context);
        String subject = "Submission Received";
        sendEmail(emails, subject, content, false, true);
    }


    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to ... '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public String sendFormRequest(String formId, String orgName, String orgId, String email) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("orgName", orgName);
        context.setVariable("formId", formId);
        context.setVariable("orgId", orgId);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("mail/formRequestEmail", context);
        String subject = "Form Request";
        sendEmail(email, subject, content, false, true);
        return "OK";
    }


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


    @Async
    public String sendFormResubmitRequest(String submissionId, String orgName, String email, String note) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("orgName", orgName);
        context.setVariable("submissionId", submissionId);
        context.setVariable("note", note);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("mail/formRequestEditEmail", context);
        String subject = orgName + " requires additional information";
        sendEmail(email, subject, content, false, true);
        return "OK";
    }

    @Async
    public void sendRejectionEmail(User user, String id, String remarks) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("userFormId", id);
        context.setVariable("user", user);
        context.setVariable("remarks", remarks);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("mail/rejectionEmail", context);
        String subject = "Form Submission Update";
        sendEmail(user.getEmail(), subject, content, false, true);

    }

    @Async
    public void sendApprovalEmail(User user, String id) {

        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("userFormId", id);
        context.setVariable("user", user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("mail/approvalEmail", context);
        String subject = "Form Submission Update";
        sendEmail(user.getEmail(), subject, content, false, true);

    }

    public void sendActivationEmailSuccess(User user) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("mail/activationEmailSuccess", context);
        String subject = "Email Activation Success";
        sendEmail(user.getEmail(), subject, content, false,true);
    }
}
