package io.proximax.kyc.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.joda.time.DateTime;

@Document(collection = "screening_submissions")

public class SS {
 @Id
 public ObjectId _id;
 public String submission_reference_id;
 public String organization_id;
 public String screening_data_hash;
 public String raw;
 public String last_update_by;
 public String user_id;
 public String form_id;
 public String status;
 public String data_hash;
 public String remarks;
 public DateTime creation_date;
 public DateTime last_update;
 public String data_api;
 public Object data_formio;
 
 // Constructors
 public SS() {}
 
    public SS(ObjectId _id, String submission_reference_id, String organization_id, String screening_data_hash, String raw, String last_update_by, String user_id, String form_id, String status, String data_hash, String remarks, DateTime creation_date, DateTime last_update, String data_api, Object data_formio) {
        this._id = _id;
        this.submission_reference_id = submission_reference_id;
        this.organization_id = organization_id;
        this.screening_data_hash = screening_data_hash;
        this.raw = raw;
        this.last_update_by = last_update_by;
        this.user_id = user_id;
        this.form_id = form_id;
        this.status = status;
        this.data_hash = data_hash;
        this.remarks = remarks;
        this.creation_date = creation_date;
        this.last_update = last_update;
        this.data_api = data_api;
        this.data_formio = data_formio;
    }
 
    // ObjectId needs to be converted to string
    public String get_id() {
        return _id.toHexString();
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String get_submission_reference_id() {
        return submission_reference_id;
    }

    public void set_submission_reference_id(String submission_reference_id) {
        this.submission_reference_id = submission_reference_id;
    }

    public String get_organization_id() {
        return organization_id;
    }

    public void set_organization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public String get_screening_data_hash() {
        return screening_data_hash;
    }

    public void set_screening_data_hash(String screening_data_hash) {
        this.screening_data_hash = screening_data_hash;
    }

    public String get_raw() {
        return raw;
    }

    public void set_raw(String raw) {
        this.raw = raw;
    }

    public String get_last_update_by() {
        return last_update_by;
    }

    public void set_last_update_by(String last_update_by) {
        this.last_update_by = last_update_by;
    }

    public String get_user_id() {
        return user_id;
    }

    public void set_user_id(String user_id) {
        this.user_id = user_id;
    }

    public String get_form_id() {
        return form_id;
    }

    public void set_form_id(String form_id) {
        this.form_id = form_id;
    }

    public String get_status() {
        return status;
    }

    public void set_status(String status) {
        this.status = status;
    }

    public String get_data_hash() {
        return data_hash;
    }

    public void set_data_hash(String data_hash) {
        this.data_hash = data_hash;
    }

    public String get_remarks() {
        return remarks;
    }

    public void set_remarks(String remarks) {
        this.remarks = remarks;
    }

    public DateTime get_creation_date() {
        return creation_date;
    }

    public void set_creation_date(DateTime creation_date) {
        this.creation_date = creation_date;
    }

    public DateTime get_last_update() {
        return last_update;
    }

    public void set_last_update(DateTime last_update) {
        this.last_update = last_update;
    }

    public String get_data_api() {
        return data_api;
    }
 
    public void set_data_api(String data_api) {
        this.data_api = data_api;
    }

    public Object get_data_formio() {
        return data_formio;
    }
 
    public void set_data_formio(Object data_formio) {
        this.data_formio = data_formio;
    }

}