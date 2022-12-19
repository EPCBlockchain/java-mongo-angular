package io.proximax.kyc.domain.application.screening;

public class JumioProperties {
    private String secret = "";
    private String url = "127.0.0.1:5001";

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
