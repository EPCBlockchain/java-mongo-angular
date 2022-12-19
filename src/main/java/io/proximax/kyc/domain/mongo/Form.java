package io.proximax.kyc.domain.mongo;

import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.form.FormVersion;
import org.json.simple.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// import net.minidev.json.JSONObject;

@Document(collection = "forms")
public class Form implements Serializable {

    // TODO: Need to create form version

   private static final long serialVersionUID = 1L;

   @Id
   private String id;

   @Field("title")
   private String title;

   @Field("display")
   private String display;

   @Field("name")
   private String name;

   @Field("path")
   private String path;

   @Field("type")
   private String type;

   @Field("project")
   private String project;

   @Field("template")
   private String template;

   @Field("components")
   private JSONArray components;

   @Field("tags")
   private List<String> tags = new ArrayList<>();;

   @Field("access")
   private String access;

   @Field("submissionAccess")
   private List<String> submissionAccess = new ArrayList<>();

   @Field("organization_id")
   private String organizationId;

   @Field("screening_form_id")
   private String screeningFormId;

   @Field("creation_date")
   private Instant creationDate;

   @Field("last_updated")
   private Instant lastUpdated;

   @Field("last_updated_by")
   private String lastUpdatedBy;

   @Field("version_details")
   private FormVersion versionDetail;

   @Field("post_back_url")
   private String postBackURL;

   @Field("unique_id_auth_url")
   private String uniqueIdAuthenticationURL;

    @Field("credential_id")
    private String credentialId;

   private Organization organization;

   public String getId() {
      return this.id;
   }
   public void setId(String value) {
      this.id = value;
   }

   public String getTitle() {
      return this.title;
   }
   public void setTitle(String value) {
      this.title = value;
   }

   public String getDisplay() {
      return this.display;
   }
   public void setDisplay(String value) {
      this.display = value;
   }

   public String getName() {
      return this.name;
   }
   public void setName(String value) {
      this.name = value;
   }

   public String getPath() {
      return this.path;
   }
   public void setPath(String value) {
      this.path = value;
   }

   public String getType() {
      return this.type;
   }
   public void setType(String value) {
      this.type = value;
   }

   public String getProject() {
      return this.project;
   }
   public void setProject(String value) {
      this.project = value;
   }

   public String getTemplate() {
      return this.template;
   }
   public void setTemplate(String value) {
      this.template = value;
   }

   public void setComponents(JSONArray set) {
      this.components = set;
   }
   public JSONArray getComponents() {
      return this.components;
   }

   public void setTags(List<String> value){
      this.tags = value;
   }
   public List<String> getTags() {
      return this.tags;
   }

   public void setAccess(String value){
      this.access = value;
   }
   public String getAccess() {
      return this.access;
   }

   public void setSubmissionAccess(List<String> submissionAccess){
      this.submissionAccess = submissionAccess;
   }
   public List<String> getSubmissionAccess() { return this.submissionAccess; }

   public void setOrganizationId(String id) {
      this.organizationId = id;
   }
   public String getOrganizationId() {
      return this.organizationId;
   }

   public String getScreeningFormId() {
      return this.screeningFormId;
   }
   public void setScreeningFormId(String value) {
      this.screeningFormId = value;
   }

   public Instant getCreationDate() { return this.creationDate;}
   public void setCreationDate(Instant date) { this.creationDate = date; }

   public FormVersion getVersionDetail() { return this.versionDetail; }
   public void setVersionDetail(FormVersion version) { this.versionDetail = version;}

   public String getPostBackURL() { return this.postBackURL; }
   public void setPostBackURL(String postBackURL) { this.postBackURL = postBackURL; }

   public Organization getOrganization() { return this.organization; }
   public void setOrganization(Organization organization) { this.organization = organization; }

   public Instant getLastUpdated() { return this.lastUpdated; }
   public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }

   public String getLastUpdatedBy() { return this.lastUpdatedBy; }
   public void setLastUpdatedBy(String lastUpdated) { this.lastUpdatedBy = lastUpdated; }

   public String getUniqueIdAuthenticationURL() { return this.uniqueIdAuthenticationURL; }
   public void setUniqueIdAuthenticationURL(String uniqueIdAuthenticationURL) { this.uniqueIdAuthenticationURL = uniqueIdAuthenticationURL; }

    public String getCredentialId() { return this.credentialId; }
    public void setCredentialId(String credentialId) { this.credentialId = credentialId; }
}
