package io.proximax.kyc.domain.mongo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "screening_submissions")
public class ScreeningSubmission extends Submission {

    @Field("submission_reference_id")
    private String submissionReferenceId;

    @Field("organization_id")
    private String organizationId;

    @Field("screening_data_hash")
    private String screeningDataHash;

    @Field("screening_date")
    private Instant screeningDate;

    @Field("raw")
    private boolean isRaw;

    @Field("screening_type")
    private String screeningType;

    @Field("last_update_by")
    private String lastUpdatedBy;

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

    public void setScreeningDataHash(String set) {
        this.screeningDataHash = set;
    }
    public String getScreeningDataHash() {
        return this.screeningDataHash;
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

    public void setLastUpdateBy(String value) { this.lastUpdatedBy = value; }
    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }
}
