package io.proximax.kyc.domain.application;

public class MailgunProperties {
    private String messageurl = "127.0.0.1:5001";
    private String username = "127.0.0.1:5001";
    private String password = "127.0.0.1:5001";
    private String apiKey = "127.0.0.1:5001";
    private Boolean enabled = false;

    public String getMessageurl() {
        return messageurl;
    }
    public void setMessageurl(String messageurl) {
        this.messageurl = messageurl;
    }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
