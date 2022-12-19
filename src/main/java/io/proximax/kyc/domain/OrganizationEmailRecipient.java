package io.proximax.kyc.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An Organization.
 */
public class OrganizationEmailRecipient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("email")
    private String email;

    @Field("types")
    private ArrayList<String> types = new ArrayList<String>();


    public void setEmail(String value) {
        this.email = value;
    }
    public String getEmail() {
        return email;
    }

    public void setTypes(ArrayList<String> value) {
        this.types = value;
    }
    public ArrayList<String> getTypes() { return types; }

}
