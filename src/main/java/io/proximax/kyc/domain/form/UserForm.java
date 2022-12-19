package io.proximax.kyc.domain.form;
import java.io.Serializable;
import java.time.Instant;

import io.proximax.kyc.domain.mongo.Form;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "userforms")
public class UserForm implements Serializable {

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

   @Field("comment")
   private String comment;

   @Field("creation_date")
   private Instant creationDate;

   private Form form;
   
   private JSONObject data;

   public String getId() {
      return this.id;
   }

   public void setId(String value) {
      this.id = value;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserId(String value) {
      this.userId = value;
   }

   public String getFormId() {
      return this.formId;
   }

   public void setFormId(String value) {
      this.formId = value;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String value) {
      this.status = value;
   }

   public String getDataHash() {
      return this.dataHash;
   }

   public void setDataHash(String value) {
      this.dataHash = value;
   }

   /**
    * @return the comment
    */
   public String getComment() {
       return comment;
   }

   /**
    * @param comment the comment to set
    */
   public void setComment(String comment) {
       this.comment = comment;
   }

   public Instant getCreationDate() {
      return this.creationDate;
   }

   public void setCreationDate(Instant value) {
      this.creationDate = value;
   }

   public Form getForm() {
      return this.form;
   }

   public void setForm(Form value) {
      this.form = value;
   }

   public JSONObject getData() {
      return this.data;
   }

   public void setData(JSONObject value) {
      this.data = value;
   }


}
