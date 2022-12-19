package io.proximax.kyc.domain;

import org.json.simple.JSONObject;

public class RequestFilter {
    private JSONObject filters;

    public void setFilters(JSONObject filters) { this.filters = filters; }
    public JSONObject getFilters() { return this.filters; }
}
