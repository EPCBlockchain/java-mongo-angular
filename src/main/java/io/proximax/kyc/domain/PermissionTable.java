package io.proximax.kyc.domain;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.proximax.kyc.domain.FormData;

@Document(collection = "Permission Table")
public class PermissionTable implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   private String id;

   @Field("owner_id")
   private String ownerId;

   @Field("requester_id")
   private String requesterId;

   @Field("user_id")
   private String userId;

   @Field("status")
   private String status;

   /**
    * @param status the status to set
    */
   public void setStatus(String status) {
       this.status = status;
   }
   /**
    * @return the status
    */
   public String getStatus() {
       return status;
   }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
        * @param ownerId the ownerId to set
        */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    /**
     * @return the ownerId
     */
    public String getOwnerId() {
        return ownerId;
    }
    /**
     * @param requesterId the requesterId to set
     */
    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }
    /**
     * @return the requesterId
     */
    public String getRequesterId() {
        return requesterId;
    }

    /**
     * @param email the email to set
     */
    public void setuserId(String userId) {
        this.userId = userId;
    }
    
    /**
     * @return the email
     */
    public String getuserId() {
        return userId;
    }

}