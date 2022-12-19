package io.proximax.kyc.domain;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Field;

public class FormData implements Serializable {

   private static final long serialVersionUID = 1L;

   @Field("id")
   private String id;

   @Field("screeningform_id")
   private String screeningform_id;

   @Field("data_hash")
   private String dataHash;

   @Field("creation_date")
   private Instant creationDate;

   public void setId(String id) {
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

    public void setScreeningFormId(String screeningform_id) {
      this.screeningform_id = screeningform_id;
   }

   public String getScreeningFormId() {
      return this.screeningform_id;
   }

   public void setDataHash(String dataHash) {
      this.dataHash = dataHash;
   }

   public String getDataHash() {
      return this.dataHash;
   }

   public void setCreationDate(Instant creationDate) {
      this.creationDate = creationDate;
   }

   public Instant
    getCreationDate() {
      return this.creationDate;
   }
}
