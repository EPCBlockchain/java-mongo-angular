package io.proximax.kyc.service;

import io.proximax.kyc.domain.User;
import java.util.ArrayList;
import java.util.HashMap;

public interface DataService {
    ArrayList<String> exportScreeningFormSubmissions( User user, String formId, HashMap<String, String> filters);
    ArrayList<String> exportKYCFormSubmissions( User user, String formId, HashMap<String, String> filters);
}
