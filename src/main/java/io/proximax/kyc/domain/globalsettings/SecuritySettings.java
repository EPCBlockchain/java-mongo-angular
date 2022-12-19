package io.proximax.kyc.domain.globalsettings;

/**
 * An authority (a security role) used by Spring Security.
 */
public class SecuritySettings {

    String postBackUrl;
    String validationUrl;
    String apiKey;

    public void setPostBackUrl(String value) { this.postBackUrl = value; }
    public String getPostBackUrl() { return this.postBackUrl; }

    public void setApiKey(String value) { this.apiKey = value; }
    public String getApiKey() { return this.apiKey; }

    public void setValidationUrl(String value) { this.validationUrl = value; }
    public String getValidationUrl() { return this.validationUrl; }
}