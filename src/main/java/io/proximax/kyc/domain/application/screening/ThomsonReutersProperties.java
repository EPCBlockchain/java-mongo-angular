package io.proximax.kyc.domain.application.screening;

public class ThomsonReutersProperties {
    private String host = "";
    private String secret = "";
    private String url = "";
    private String key = "";
    private String groups = "";
    private String cases = "";
    private String profiles = "";

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getGroups() { return groups; }
    public void setGroups(String groups) { this.groups = groups; }

    public String getCases() { return cases; }
    public void setCases(String cases) { this.cases = cases; }

    public String getProfiles() { return profiles; }
    public void setProfiles(String profiles) { this.profiles = profiles; }
}
