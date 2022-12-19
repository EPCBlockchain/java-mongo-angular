package io.proximax.kyc.domain.form;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.proximax.kyc.domain.mongo.Form;

public class FormGridDTO {
    private Integer count;
    private List<Form> rows = new ArrayList<Form>();

    public void setCount(Integer value) { this.count = value; }
    public Integer getCount() { return this.count; }


    public void setRows(List<Form> value) { this.rows = value; }
    public List<Form> getRows() { return this.rows; }
}
