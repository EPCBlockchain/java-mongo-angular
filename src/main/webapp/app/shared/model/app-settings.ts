import { EmailTemplateEntity } from './email-template.entity';

export class AppSettingsEntity {
    emailSettings: EmailSettingsEntity = new EmailSettingsEntity();
    securitySettings: SecuritySettingsEntity = new SecuritySettingsEntity();
}

export class EmailSettingsEntity {
    activationEmail: EmailTemplateEntity = new EmailTemplateEntity();
    activationEmailSuccess: EmailTemplateEntity = new EmailTemplateEntity();
    approvalEmail: EmailTemplateEntity = new EmailTemplateEntity();
    creationEmail: EmailTemplateEntity = new EmailTemplateEntity();
    formRequestEmailEdit: EmailTemplateEntity = new EmailTemplateEntity();
    formRequestEmail: EmailTemplateEntity = new EmailTemplateEntity();
    passwordResetEmail: EmailTemplateEntity = new EmailTemplateEntity();
    rejectionEmail: EmailTemplateEntity = new EmailTemplateEntity();
    statusEmail: EmailTemplateEntity = new EmailTemplateEntity();
    submissionReceivedEmail: EmailTemplateEntity = new EmailTemplateEntity();
}

export class SecuritySettingsEntity {
    public postBackUrl = '';
    public validationUrl = '';
    public apiKey = '';
}
