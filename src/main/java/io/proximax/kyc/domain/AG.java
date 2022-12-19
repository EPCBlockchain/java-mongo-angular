package io.proximax.kyc.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "application_general")

public class AG {
 @Id
 public ObjectId _id;
 public String action;
 public String type;
 public String active;
 
 // Constructors
 public AG() {}
 
    public AG(ObjectId _id, String action, String type, String active) {
        this._id = _id;
        this.action = action;
        this.type = type;
        this.active = active;
    }
 
    // ObjectId needs to be converted to string
    public String get_id() {
        return _id.toHexString();
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }
 
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
 
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
 
    public String getActive() {
        return active;
    }
    public void setActive(String active) {
        this.active = active;
    }
}