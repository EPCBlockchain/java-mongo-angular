package io.proximax.kyc.domain;

import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An Organization.
 */
public class OrganizationSecurity implements Serializable {

   private static final long serialVersionUID = 1L;

   @Field("post_back_url")
   private String postBackUrl;

   @Field("validation_url")
   private String validationUrl;

    @Field("allowed_hosts")
    private String allowedHosts;

    @Field("api_key")
    private String apiKey;

    public String getPostBackUrl() { return postBackUrl; }
    public void setPostBackUrl(String postBackUrl) { this.postBackUrl = postBackUrl; }

    public String getValidationUrl() { return validationUrl; }
    public void setValidationUrl(String validationUrl) { this.validationUrl = validationUrl; }

    public String getAllowedHosts() { return allowedHosts; }
    public void setAllowedHosts(String allowedHosts) { this.allowedHosts = allowedHosts; }

    public void setApiKey(String key) { this.apiKey = key; }
    public String getApiKey() {
        return this.apiKey;
    }

}
