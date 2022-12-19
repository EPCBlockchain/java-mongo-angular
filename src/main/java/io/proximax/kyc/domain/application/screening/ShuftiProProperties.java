package io.proximax.kyc.domain.application.screening;

public class ShuftiProProperties {
    private String clientid = "";
    private String secret = "";
    private String url = "";

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    public String getClientid() { return clientid; }

    public void setClientid(String clientid) { this.clientid = clientid; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
