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
@Document(collection = "user_submitted_form")
public class UserSubmittedForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("user_id")
    private String userId;

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
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the orgId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
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
