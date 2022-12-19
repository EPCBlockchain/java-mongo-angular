package io.proximax.kyc.domain.application;

public class FormProperties {
    private String validationURL = "";
    private String postBackURL = "";

    public void setPostBackURL(String postBackURL) { this.postBackURL = postBackURL; }
    public String getPostBackURL() { return this.postBackURL; }

    public void setValidationURL(String validationURL) { this.validationURL = validationURL; }
    public String getValidationURL() { return this.validationURL; }
}
