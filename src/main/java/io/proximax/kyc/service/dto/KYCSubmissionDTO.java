package io.proximax.kyc.service.dto;

import io.proximax.kyc.domain.mongo.KYCSubmission;

public class KYCSubmissionDTO extends SubmissionDTO{

    private boolean isExternal = false;
    private String credentialId;

    public KYCSubmissionDTO() { }

    public KYCSubmissionDTO(KYCSubmission submission) {
        this.setCreationDate(submission.getCreationDate());
        this.setLastUpdate(submission.getLastUpdate());
        this.setFormId(submission.getFormId());
        this.setStatus(submission.getStatus());
        this.setId(submission.getId());
        this.setUserId(submission.getUserId());
        this.setCredentialId(submission.getCredentialId());
        this.setIsExternal(submission.getIsExternal());
        this.setRemarks(submission.getRemarks());
    }

    public KYCSubmissionDTO(KYCSubmission submission, boolean full) {
        this.setCreationDate(submission.getCreationDate());
        this.setLastUpdate(submission.getLastUpdate());
        this.setFormId(submission.getFormId());
        this.setStatus(submission.getStatus());
        this.setId(submission.getId());
        this.setUserId(submission.getUserId());
        this.setIsExternal(submission.getIsExternal());
        this.setCredentialId(submission.getCredentialId());
    }

    public boolean getIsExternal() {  return this.isExternal; }
    public void setIsExternal(boolean value) { this.isExternal = value; }

    public String getCredentialId() {  return this.credentialId; }
    public void setCredentialId(String value) { this.credentialId = value; }
}
