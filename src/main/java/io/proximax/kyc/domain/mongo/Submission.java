package io.proximax.kyc.domain.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("form_id")
    private String formId;

    @Field("status")
    private String status;

    @Field("data_hash")
    private String dataHash;

    @Field("remarks")
    private String remarks;

    @Field("data_api")
    private String data_api;

    @Field("data_formio")
    private Object data_formio;

    @Field("filter_hashes")
    private List<String> filterHashes = new ArrayList<>();

    @Field("creation_date")
    private Instant creationDate;

    @Field("last_update")
    private Instant lastUpdate;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

    public String getFormId() {
        return formId;
    }
    public void setFormId(String formId) {
        this.formId = formId;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }
    public String getDataHash() {
        return dataHash;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setCreationDate(Instant date) { this.creationDate = date; }
    public Instant getCreationDate() {
        return this.creationDate;
    }

    public void setFilterHash(List<String> value) { this.filterHashes = value; }
    public List<String> getFilterHashes() { return this.filterHashes; }

    public void setLastUpdate(Instant date) {
        this.lastUpdate = date;
    }
    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getRemarks() {
        return this.remarks;
    }

    public void setDataApi(String data_api) { this.data_api = data_api; }
    public String getDataApi() {
        return this.data_api;
    }

    public void setDataFormio(Object data_api) { this.data_formio = data_api; }
    public Object getDataFormio() {
        return this.data_formio;
    }    
    

}
