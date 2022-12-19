package io.proximax.kyc.domain.globalsettings;

/**
 * An authority (a security role) used by Spring Security.
 */
public class EmailSettings {

    EmailTemplate formRequestEmail;
    EmailTemplate formRequestEmailEdit;
    EmailTemplate approvalEmail;
    EmailTemplate rejectionEmail;
    EmailTemplate submissionReceivedEmail;
    EmailTemplate statusEmail;

    EmailTemplate activationEmail;
    EmailTemplate activationEmailSuccess;
    EmailTemplate creationEmail;
    EmailTemplate passwordResetEmail;

    public void setFormRequestEmail(EmailTemplate template) { this.formRequestEmail = template; }
    public EmailTemplate getFormRequestEmail() { return this.formRequestEmail; }

    public void setFormRequestEmailEdit(EmailTemplate template) { this.formRequestEmailEdit = template; }
    public EmailTemplate getFormRequestEmailEdit() { return this.formRequestEmailEdit; }

    public void setApprovalEmail(EmailTemplate template) { this.approvalEmail = template; }
    public EmailTemplate getApprovalEmail() { return this.approvalEmail; }

    public void setRejectionEmail(EmailTemplate template) { this.rejectionEmail = template; }
    public EmailTemplate getRejectionEmail() { return this.rejectionEmail; }

    public void setSubmissionReceivedEmail(EmailTemplate template) { this.submissionReceivedEmail = template; }
    public EmailTemplate getSubmissionReceivedEmail() { return this.submissionReceivedEmail; }

    public void setStatusEmail(EmailTemplate template) { this.statusEmail = template; }
    public EmailTemplate getStatusEmail() { return this.statusEmail; }

    public void setActivationEmail(EmailTemplate template) { this.activationEmail = template; }
    public EmailTemplate getActivationEmail() { return this.activationEmail; }

    public void setActivationEmailSuccess(EmailTemplate template) { this.activationEmailSuccess = template; }
    public EmailTemplate getActivationEmailSuccess() { return this.activationEmailSuccess; }

    public void setCreationEmail(EmailTemplate template) { this.creationEmail = template; }
    public EmailTemplate getCreationEmail() { return this.creationEmail; }

    public void setPasswordResetEmail(EmailTemplate template) { this.passwordResetEmail = template; }
    public EmailTemplate getPasswordResetEmail() { return this.passwordResetEmail; }
}