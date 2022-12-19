package io.proximax.kyc.domain;

import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.service.dto.KYCSubmissionDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class SubmissionCredential implements Serializable {

    private String id;

    private Credential credential;

    private KYCSubmissionDTO submission;

    public void setCredential(Credential credential) {
        this.credential = credential;
    }
    public Credential getCredential() {
        return this.credential;
    }

    public void setSubmission(KYCSubmissionDTO submission) {
        this.submission = submission;
    }

    public KYCSubmissionDTO getSubmission(){
        return this.submission;
    }
}
