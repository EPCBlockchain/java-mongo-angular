package io.proximax.kyc.service.dto;

import org.json.simple.JSONObject;

public class ScreeningResultDTO {

    private String type;
    private JSONObject result;

    public void setType(String value) { this.type = value; }
    public String getType() { return this.type; }

    public void setResult(JSONObject result) { this.result = result; }
    public JSONObject getResult() { return this.result; }
}
