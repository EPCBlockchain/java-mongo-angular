package io.proximax.kyc.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
// import java.time.LocalDate;
// import java.util.HashSet;
import java.util.Objects;
// import java.util.Set;

/**
 * A Form.
 */
@Document(collection = "organization_submitted_form")
public class OrganizationSubmittedForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("org_id")
    private String organizationId;

    @Field("form")
    private FormIO form;

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the organizationId
     */
    public String getOrganizationId() {
        return this.organizationId;
    }

    /**
     * @param organizationId the orgId to set
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return the form
     */
    public FormIO getForm() {
        return form;
    }

    /**
     * @param form the form to set
     */
    public void setForm(FormIO form) {
        this.form = form;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
