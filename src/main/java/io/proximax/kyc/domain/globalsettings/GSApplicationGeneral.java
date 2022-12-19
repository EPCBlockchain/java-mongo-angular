package io.proximax.kyc.domain.globalsettings;

import java.io.Serializable;
import io.proximax.kyc.config.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import javax.validation.constraints.Email;
import org.springframework.data.mongodb.core.index.Indexed;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;
import java.time.Instant;

/**
 * An authority (a security role) used by Spring Security.
 */
@Document(collection = "application_general")
// @JsonPropertyOrder({"id"})
public class GSApplicationGeneral implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("action")
    private String action;

    @Size(max = 100)
    @Field("type")
    private String type;

    @Field("active")
    private String active;

    // @JsonIgnore
    // private List<Authority> authorities = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAction(String action) { 
        this.action = action; 
    }
    
    public String getAction() { 
        return this.action; 
    }

    public void setType(String type) { 
        this.type = type; 
    }
    
    public String getType() { 
        return this.type; 
    }

    public void setActive(String active) { 
        this.active = active; 
    }
    
    public String getActive() { 
        return this.active; 
    }
}
