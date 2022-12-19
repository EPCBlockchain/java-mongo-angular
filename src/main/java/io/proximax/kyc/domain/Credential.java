package io.proximax.kyc.domain;

import io.proximax.kyc.domain.screening.keys.ScreeningKeyPairs;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An Organization.
 */
@Document(collection = "credentials")
public class Credential implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("descrition")
    private String description;

    @Field("code")
    private String code;

    @Field("image")
    private String image;

    @Field("organization_id")
    private String organizationId;

    @Field("creation_date")
    private Instant creationDate;

    @Field("status")
    private String status;

    private String imageFile;

    private Map<String, String> contents = new HashMap<>();

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

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return this.image;
    }
    public void setImage(String value) {this.image = value;}

    public String getImageFile() {
        return this.imageFile;
    }
    public void setImageFile(String value) {
        this.imageFile = value;
    }

    public String getOrganizationId() {
        return this.organizationId;
    }
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }
    public void setCreationDate(Instant date) {
        this.creationDate = date;
    }

    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getContents() {
        return this.contents;
    }
    public void setContents(Map<String, String> contents) { this.contents = contents; }

}
