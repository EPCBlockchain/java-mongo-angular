package io.proximax.kyc.domain;

import io.proximax.kyc.domain.screening.keys.ScreeningKeyPairs;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;

/**
 * An Organization.
 */
@Document(collection = "organizations")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("status")
    private String status;

    @Field("profile")
    private OrganizationProfile profile = new OrganizationProfile();

    @Field("security")
    private OrganizationSecurity security = new OrganizationSecurity();

    @Field("email")
    private String email;

    @Field("website")
    private String website;

    @Field("creation_date")
    private Instant creationDate;

    @Field("screening_keys")
    private ScreeningKeyPairs screeningKeys = new ScreeningKeyPairs();

    @Field("email_recipients")
    private ArrayList<OrganizationEmailRecipient> emailRecipients = new ArrayList<>();

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setScreeningKeys(ScreeningKeyPairs screeningKeys) {
        this.screeningKeys = screeningKeys;
    }
    public ScreeningKeyPairs getScreeningKeys() { return this.screeningKeys; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }

    public void setSecurity(OrganizationSecurity security) { this.security = security; }
    public OrganizationSecurity getSecurity() { return security; }

    public void setCreationDate(Instant date) {
        this.creationDate = date;
    }
    public Instant getCreationDate() {
        return this.creationDate;
    }

    public void setEmailRecipients(ArrayList<OrganizationEmailRecipient> emailRecipients) { this.emailRecipients = emailRecipients; }
    public ArrayList<OrganizationEmailRecipient> getEmailRecipients () { return this.emailRecipients; }
}
