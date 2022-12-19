package io.proximax.kyc.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Form.
 */
@Document(collection = "formio")
public class FormIO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("screeningform_id")
    private String screeningId;

    @Field("fullname")
    private String fullname;

    @Field("address")
    private String address;

    @Field("birthday")
    private LocalDate birthday;

    @Field("status")
    private String status;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public FormIO status(String status) {
        this.status = status;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
