package io.proximax.kyc.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class BatchScreeningData implements Serializable {

    private static final long serialVersionUID = 1L;
    ArrayList<String> submittedIds;
    private String screeningType;
    
    public String getScreeningType() {
        return screeningType;
    }
    public ArrayList<String> getSubmittedIds() {
        return submittedIds;
    }

    public void setScreeningType(String value) {
        this.screeningType = value;
    }
    public void setSubmittedIds(ArrayList<String> value) {
        this.submittedIds = value;
    }
    
}
