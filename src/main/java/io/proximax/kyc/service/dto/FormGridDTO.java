package io.proximax.kyc.service.dto;

import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.mongo.Form;

import java.time.Instant;

public class FormGridDTO {

    private String id;
    private String title;
    private Instant creationDate;
    private Instant lastUpdated;
    private Organization organization;

    public FormGridDTO() {}

    public FormGridDTO(Form form) {
        this.setCreationDate(form.getCreationDate());
        this.setId(form.getId());
        this.setTitle(form.getTitle());
        this.setOrganization(form.getOrganization());
        this.setLastUpdated(form.getLastUpdated());
    }

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String value) {
        this.title = value;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }
    public void setCreationDate(Instant value) {
        this.creationDate = value;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }
    public void setLastUpdated(Instant value) {
        this.lastUpdated = value;
    }

    public Organization getOrganization() {
        return this.organization;
    }
    public void setOrganization(Organization value) {
        this.organization = value;
    }
}
