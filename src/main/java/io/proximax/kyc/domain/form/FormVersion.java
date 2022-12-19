package io.proximax.kyc.domain.form;

import java.time.Instant;
import java.util.UUID;

public class FormVersion {
    private Integer version;
    private String referenceFormId;
    private Instant versionCreated;
    private String code;

    public FormVersion() {
        version = 1;
        code = UUID.randomUUID().toString();
    }

    public void setVersion(Integer value) { this.version = value; }
    public Integer getVersion() { return this.version; }

    public void setReferenceFormId(String value) { this.referenceFormId = value; }
    public String getReferenceFormId() { return this.referenceFormId; }

    public void setVersionCreated(Instant value) { this.versionCreated = value; }
    public Instant getVersionCreated() { return this.versionCreated; }

    public void setCode(String value) { this.code = value; }
    public String getCode() { return this.code; }
}
