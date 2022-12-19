package io.proximax.kyc.domain.globalsettings;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * An authority (a security role) used by Spring Security.
 */
@Document(collection = "application_settings")
public class GlobalSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("email-settings")
    private EmailSettings emailSettings;

    @Field("security-settings")
    private SecuritySettings securitySettings;

    public void setEmailSettings(EmailSettings settings) { this.emailSettings = settings; }
    public EmailSettings getEmailSettings() { return this.emailSettings; }

    public void setSecuritySettings(SecuritySettings settings) { this.securitySettings = settings; }
    public SecuritySettings getSecuritySettings() { return this.securitySettings; }
}
