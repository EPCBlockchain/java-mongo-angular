package io.proximax.kyc.service.dto;

import io.proximax.kyc.domain.keypairs.SubmissionColumnKeyPair;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubmissionGridDTO<T> {
    private List<SubmissionColumnKeyPair> columns = new ArrayList<>();
    private List<T> rows = new ArrayList<>();
    private JSONObject requestData = new JSONObject();

    public void setColumns(List<SubmissionColumnKeyPair> columns) {
        this.columns = columns;
    }
    public List<SubmissionColumnKeyPair> getColumns() {
        return this.columns;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
    public List<T> getRows() {
        return this.rows;
    }

    public void setRequestData(JSONObject value) { this.requestData = value; }
    public JSONObject getRequestData() { return this.requestData; }

    public SubmissionGridDTO() {}
    public SubmissionGridDTO(List<T> rows) { this.setRows(rows); }
}
