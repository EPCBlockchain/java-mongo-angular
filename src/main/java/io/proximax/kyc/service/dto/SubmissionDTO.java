package io.proximax.kyc.service.dto;

import io.proximax.kyc.domain.mongo.Form;
import org.json.simple.JSONObject;

import java.time.Instant;

public class SubmissionDTO {

    private String id;

    private String userId;

    private String formId;

    private String status;

    private Instant creationDate;

    private Instant lastUpdate;

    private JSONObject data;

    private String remarks;

    private Form form;

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

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setCreationDate(Instant date) {
        this.creationDate = date;
    }
    public Instant getCreationDate() {
        return this.creationDate;
    }

    public void setLastUpdate(Instant date) {
        this.lastUpdate = date;
    }
    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
    public JSONObject getData() {
        return data;
    }

    public Form getForm() {
        return this.form ;
    }
    public void setForm(Form form) {
        this.form = form;
    }

    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getRemarks() {
        return this.remarks;
    }
}
