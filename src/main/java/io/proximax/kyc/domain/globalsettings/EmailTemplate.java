package io.proximax.kyc.domain.globalsettings;
import java.time.Instant;

import io.proximax.kyc.domain.Organization;

public class EmailTemplate {
   private String template;
   private String status;
   private String type;
   private String subject;
   private Instant lastUpdated;
   private String lastUpdatedBy;
   private Organization organization;

   public String getTemplate() { return this.template; }
   public void setTemplate(String value) { this.template = value; }

   public String getType() { return this.type; }
   public void setType(String value) { this.type = value; }

   public String getSubject() { return this.subject; }
   public void setSubject(String value) { this.subject = value; }

   public String getStatus() { return this.status; }
   public void setStatus(String value) { this.status = value; }

   public Organization getOrganization() { return this.organization; }
   public void setOrganization(Organization organization) { this.organization = organization; }

   public Instant getLastUpdated() { return this.lastUpdated; }
   public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }

   public String getLastUpdatedBy() { return this.lastUpdatedBy; }
   public void setLastUpdatedBy(String lastUpdated) { this.lastUpdatedBy = lastUpdated; }
}
