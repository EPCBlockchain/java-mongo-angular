package io.proximax.kyc.domain.application;

public class SecurityProperties {
    private String key = "";
    private String apiKey = "";
    private Boolean usingGlobal = false;

    public void setKey(String key) { this.key = key; }
    public String getKey() { return this.key; }

    public void setApiKey(String key) { this.apiKey = key; }
    public String getApiKey() { return this.apiKey; }

    public Boolean getUsingGlobal() { return usingGlobal; }
    public void setUsingGlobal(Boolean usingGlobal) { this.usingGlobal = usingGlobal; }
    
}
