package io.proximax.kyc.service.dto;

import io.proximax.kyc.domain.mongo.ScreeningSubmission;
import org.json.simple.JSONObject;

import java.time.Instant;

public class ScreeningSubmissionDTO extends SubmissionDTO {

    private String submissionReferenceId;

    private String organizationId;

    private JSONObject screeningData;

    private Instant screeningDate;

    private boolean isRaw;

    private String screeningType;
   
    private String lastUpdatedBy;

    private String dataApi;

    private Object dataFormio;

    public ScreeningSubmissionDTO() {}

    public ScreeningSubmissionDTO(ScreeningSubmission submission) {
        this.setIsRaw(submission.getIsRaw());
        this.setOrganizationId(submission.getOrganizationId());
        this.setScreeningDate(submission.getScreeningDate());
        this.setScreeningType(submission.getScreeningType());
        this.setSubmissionReferenceId(submission.getSubmissionReferenceId());
        this.setFormId(submission.getFormId());
        this.setId(submission.getId());
        this.setOrganizationId(submission.getOrganizationId());
        this.setCreationDate(submission.getCreationDate());
        this.setLastUpdate(submission.getLastUpdate());
        this.setStatus(submission.getStatus());
        this.setUserId(submission.getUserId());
        this.setRemarks(submission.getRemarks());
        this.setLastUpdatedBy(submission.getLastUpdatedBy());
        this.setDataApi(submission.getDataApi());
        this.setDataFormio(submission.getDataFormio());
    }

    public Instant getScreeningDate() {
        return this.screeningDate;
    }
    public void setScreeningDate(Instant set) {
        this.screeningDate = set;
    }

    public void setOrganizationId(String set) {
        this.organizationId = set;
    }
    public String getOrganizationId() {
        return this.organizationId;
    }

    public void setSubmissionReferenceId(String set) {
        this.submissionReferenceId = set;
    }
    public String getSubmissionReferenceId() {
        return this.submissionReferenceId;
    }

    public void setScreeningData(JSONObject set) {
        this.screeningData = set;
    }
    public JSONObject getScreeningData() {
        return this.screeningData;
    }

    public boolean getIsRaw() {
        return this.isRaw;
    }
    public void setIsRaw(boolean isRaw) {
        this.isRaw = isRaw;
    }

    public String getScreeningType() {
        return this.screeningType;
    }
    public void setScreeningType(String screeningType) {
        this.screeningType = screeningType;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }
    public void setLastUpdatedBy(String value) {
        this.lastUpdatedBy = value;
    }
    
    public String getDataApi() {
        return this.dataApi;
    }
    public void setDataApi(String value) {
        this.dataApi = value;
    }

    public Object getDataFormio() {
        return this.dataFormio;
    }
    public void setDataFormio(Object value) {
        this.dataFormio = value;
    }

}
