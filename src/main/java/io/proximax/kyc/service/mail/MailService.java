package io.proximax.kyc.service.mail;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import io.github.jhipster.config.JHipsterProperties;
import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.constants.EmailTemplateDefaultConstants;
import io.proximax.kyc.domain.constants.EmailTemplateTypeConstants;
import io.proximax.kyc.domain.globalsettings.EmailTemplate;
import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.domain.mongo.ScreeningSubmission;
import io.proximax.kyc.service.GlobalSettingsService;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";

    private final MailGunService mailGunService;
    private final SMTPMailService smtpMailService;
    private final ApplicationProperties applicationProperties;
    private final GlobalSettingsService globalSettingsService;
    private final JHipsterProperties jHipsterProperties;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private Integer mailType = 0;
    // smtp = 0;
    // mailgun = 1;

    public MailService(JHipsterProperties jHipsterProperties, MailGunService mailGunService, SMTPMailService smtpMailService,
                        GlobalSettingsService globalSettingsService,
                        SpringTemplateEngine templateEngine, ApplicationProperties applicationProperties, MessageSource messageSource) {
        if (applicationProperties.getMailgun().getEnabled()) {
            this.mailType = 1;
        }

        this.smtpMailService = smtpMailService;
        this.globalSettingsService = globalSettingsService;
        this.mailGunService = mailGunService;
        this.applicationProperties = applicationProperties;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
    }

    @Async
    public void sendSubmissionReceivedEmail(String emails, KYCSubmission kycSubmission) {
        EmailTemplate template = this.globalSettingsService.getEmailTemplate(EmailTemplateTypeConstants.SUBMISSION_RECEIVED_EMAIL);
        String templateBody = template.getTemplate();
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.SUBMISSION_ID, kycSubmission.getId());
        String content = templateBody;
        String subject = template.getSubject();
        sendEmail(emails, subject, content, false, true);
    }

    @Async
    public void sendRejectionEmail(User user, String id, String remarks) {
        EmailTemplate template = this.globalSettingsService.getEmailTemplate(EmailTemplateTypeConstants.REJECTION_EMAIL);
        String templateBody = template.getTemplate();
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.ACCOUNT_LOGIN, user.getLogin());
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.SUBMISSION_ID, id);
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.NOTE, remarks);
        String content = templateBody;
        String subject = template.getSubject();
        sendEmail(user.getEmail(), subject, content, false, true);
    }


    @Async
    public void sendApprovalEmail(User user, String id) {
        EmailTemplate template = this.globalSettingsService.getEmailTemplate(EmailTemplateTypeConstants.APPROVAL_EMAIL);
        String templateBody = template.getTemplate();
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.ACCOUNT_LOGIN, user.getLogin());
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.SUBMISSION_ID, id);
        String content = templateBody;
        String subject = template.getSubject();
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public String sendFormRequest(String formId, String orgName, String orgId, String email) {
        EmailTemplate template = this.globalSettingsService.getEmailTemplate(EmailTemplateTypeConstants.FORM_REQUEST_EMAIL);
        String templateBody = template.getTemplate();
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.ORGANIZATION_ID, orgId);
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.FORM_ID, formId);
        String content = templateBody;
        String subject = template.getSubject();
        sendEmail(email, subject, content, false, true);
        return "OK";
    }

    @Async
    public String sendFormResubmitRequest(String submissionId, String orgName, String email, String note) {
        EmailTemplate template = this.globalSettingsService.getEmailTemplate(EmailTemplateTypeConstants.FORM_REQUEST_EMAIL_EDIT);
        String templateBody = template.getTemplate();
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.SUBMISSION_ID, submissionId);
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.NOTE, note);
        String content = templateBody;
        String subject = template.getSubject();
        sendEmail(email, subject, content, false, true);
        return "OK";
    }

    @Async
    public void sendStatusEmail(String emails, ScreeningSubmission screeningSubmission) {
        EmailTemplate template = this.globalSettingsService.getEmailTemplate(EmailTemplateTypeConstants.STATUS_EMAIL);
        String templateBody = template.getTemplate();
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.SUBMISSION_ID, screeningSubmission.getId());
        String content = templateBody;
        String subject = template.getSubject();
        sendEmail(emails, subject, content, false, true);
    }

    // ACCOUNT

    @Async
    public void sendActivationEmail(User user) {
        EmailTemplate template = this.globalSettingsService.getEmailTemplate(EmailTemplateTypeConstants.ACTIVATION_EMAIL);
        String templateBody = template.getTemplate();
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.ACCOUNT_LOGIN, user.getLogin());
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.ACTIVATION_KEY, user.getActivationKey());
        String content = templateBody;
        String subject = template.getSubject();
        sendEmail(user.getEmail(), subject, content, false,true);
    }

    @Async
    public void sendActivationEmailSuccess(User user) {
        EmailTemplate template = this.globalSettingsService.getEmailTemplate(EmailTemplateTypeConstants.ACTIVATION_EMAIL_SUCCESS);
        String templateBody = template.getTemplate();
        String preContent = "<img src='https://www.proximax.io/user/themes/proximaxvrs1/images/logo.png' style='height: 50px;' ><br><br>";
        String content = preContent + templateBody.replace(EmailTemplateDefaultConstants.ACCOUNT_LOGIN, user.getLogin());
        String subject = template.getSubject();
        sendEmail(user.getEmail(), subject, content, false,true);
    }

    @Async
    public void sendCreationEmail(User user) {
        EmailTemplate template = this.globalSettingsService.getEmailTemplate(EmailTemplateTypeConstants.CREATION_EMAIL);
        String templateBody = template.getTemplate();
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.ACCOUNT_LOGIN, user.getLogin());
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.PASSWORD_RESET_KEY, user.getResetKey());
        String content = templateBody;
        String subject = template.getSubject();
        sendEmail(user.getEmail(), subject, content, false,true);
    }

    @Async
    public void sendPasswordResetMail(User user) {
        EmailTemplate template = this.globalSettingsService.getEmailTemplate(EmailTemplateTypeConstants.PASSWORD_RESET_EMAIL);
        String templateBody = template.getTemplate();
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.ACCOUNT_LOGIN, user.getLogin());
        templateBody = templateBody.replace(EmailTemplateDefaultConstants.PASSWORD_RESET_KEY, user.getResetKey());
        String content = templateBody;
        String subject = template.getSubject();
        sendEmail(user.getEmail(), subject, content, false,true);
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        switch(this.mailType) {
            case 1:
                mailGunService.sendEmail(to, subject, content, isMultipart, isHtml); break;
            default:
                smtpMailService.sendEmail(to, subject, content, isMultipart, isHtml); break;
        }
    }
}
