package io.proximax.kyc.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import io.proximax.xpx.utils.CryptoUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.security.InvalidKeyException;

public class FormIOData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    
    private String screeningId;

    private String formId;
    
    private String data;

    private String state;

    private Boolean saved;

    public void setId(String value) {
        this.id = value;
    }

    public String getId() {
        return this.id;
    }

    public void setScreeningId(String value) {
        this.screeningId = value;
    }

    public String getScreeningId() {
        return this.screeningId;
    }

    public void setFormId(String value) {
        this.formId = value;
    }

    public String getFormId() {
        return this.formId;
    }

    public void setData(String value) {
        this.data = value;
    }

    public String getData() {
        return this.data;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public Boolean getSaved() {
        return this.saved;
    }
}
