package io.proximax.kyc.domain.mongo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "kyc_submissions")
public class KYCSubmission extends Submission {

    @Field("is_external")
    private boolean isExternal = false;

    @Field("credential_id")
    private String credentialId;

    public Boolean getIsExternal() {  return this.isExternal; }
    public void setIsExternal(Boolean value) { this.isExternal = value; }

    public String getCredentialId() {  return this.credentialId; }
    public void setCredentialId(String credentialId) { this.credentialId = credentialId; }
}
