package io.proximax.kyc.service.impl;

import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.constants.ScreeningTypeConstants;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.domain.mongo.ScreeningSubmission;
import io.proximax.kyc.service.DataService;
import io.proximax.kyc.service.FormService;
import io.proximax.kyc.service.SubmissionService;
import io.proximax.kyc.service.storage.StorageService;
import io.proximax.kyc.service.util.EncryptionUtil;
import io.proximax.kyc.service.util.FormUtil;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    private final Logger log = LoggerFactory.getLogger(DataServiceImpl.class);
    private final SubmissionService submissionService;
    private final StorageService storageService;
    private final FormService formService;
    private final EncryptionUtil encryptionUtil;
    private final FormUtil formUtil;

    public DataServiceImpl(SubmissionService submissionService, FormService formService, FormUtil formUtil,
            StorageService storageService, EncryptionUtil encryptionUtil) {
        this.submissionService = submissionService;
        this.formService = formService;
        this.storageService = storageService;
        this.encryptionUtil = encryptionUtil;
        this.formUtil = formUtil;
    }

    public ArrayList<String> exportKYCFormSubmissions(User user, String formId, HashMap<String, String> filters) {
        List<KYCSubmission> forms = getAllKYCFormSubmissions(user, formId, filters);
        Form form = formService.findOne(formId).get();
        int printHeader = 0;
        ArrayList<String> csvDataReturn = new ArrayList<String>();
        for (KYCSubmission subform : forms) {
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<String> values = new ArrayList<String>();
            String ipfsHash = storageService.get(subform.getDataHash());
            JSONObject data = this.encryptionUtil.decryptToJSONObject(ipfsHash);
            this.formUtil.findLabelAndValues(form.getComponents(), data, labels, values);

            if (printHeader == 0) {
                String header = "User ID,Form ID,Creation Date,Status,";
                for (String s : labels) {
                    s = s.replaceAll(",", " ");
                    header += s + ",";
                }
                header = header.substring(0, header.length() - 1);
                csvDataReturn.add(header);

                printHeader = 1;
            }
            String listString = "";
            listString += subform.getUserId();
            listString += ",";
            listString += subform.getFormId();
            listString += ",";
            listString += subform.getCreationDate().toString();
            listString += ",";
            listString += subform.getStatus();
            listString += ",";
            if (values != null) {
                for (String s : values) {
                    listString += s + ",";
                }
                listString = listString.substring(0, listString.length() - 1);
                csvDataReturn.add(listString);

            }

        }
        return csvDataReturn;
    }

    public ArrayList<String> exportScreeningFormSubmissions(User user, String formId, HashMap<String, String> filters) {
        List<ScreeningSubmission> forms = getAllScreeningFormSubmissions(user, formId, filters);
        Form form = formService.findOne(formId).get();
        int printHeader = 0;
        String screening_result = "";
        String screening_detail = "";
        ArrayList<String> csvDataReturn = new ArrayList<String>();
        for (ScreeningSubmission subform : forms) {
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<String> values = new ArrayList<String>();
            String ipfsHash = storageService.get(subform.getDataHash());
            JSONObject data = this.encryptionUtil.decryptToJSONObject(ipfsHash);
            this.formUtil.findLabelAndValues(form.getComponents(), data, labels, values);

            if (printHeader == 0) {
                String header = "User ID,Organization ID,Form ID, Submittion Reference ID,Creation Date,Status,";
                for (String s : labels) {
                    s = s.replaceAll(",", " ");
                    header += s + ",";
                }
                header += "Screening Type,Screening Date, Screening Result, Screening Detail";
                csvDataReturn.add(header);

                printHeader = 1;
            }
            String listString = "";
            listString += subform.getUserId();
            listString += ",";
            listString += subform.getOrganizationId();
            listString += ",";
            listString += subform.getFormId();
            listString += ",";
            listString += subform.getSubmissionReferenceId();
            listString += ",";
            listString += subform.getCreationDate().toString();
            listString += ",";
            listString += subform.getStatus();
            listString += ",";

            if (values != null) {
                for (String s : values) {
                    listString += s + ",";
                }
            }
            String screeningType = subform.getScreeningType();
            listString += screeningType;
            listString += ",";
            Instant screeningDate = subform.getScreeningDate();
            if (screeningDate != null)
                listString += screeningDate.toString();
            else
                listString += " ";
            listString += ",";

            /* Parse and update screening result, detail */
            String ipfsScreening = subform.getScreeningDataHash();
            if (ipfsScreening != null) {
                String ipfsScreeningHash = storageService.get(ipfsScreening);
                JSONObject screeningData = this.encryptionUtil.decryptToJSONObject(ipfsScreeningHash);

                switch (screeningType) {
                    case ScreeningTypeConstants.IDM: {

                    }
                    case ScreeningTypeConstants.SHUFTI_PRO: {
                        screening_result = (String) screeningData.get("event");
                        if (screening_result != null) {
                            if (screening_result.contains("accepted"))
                                screening_result = "ACCEPTED";
                            else
                                screening_result = "DECLINED";
                        } else
                            screening_result = "MANUAL_REVIEW";
                        screening_detail = (String) screeningData.get("verification_result");
                        screening_detail = screening_detail.replaceAll("},", "}\n");
                        screening_detail = screening_detail.replaceAll(",", "");
                    }
                    case ScreeningTypeConstants.THOMSON_REUTERS: {

                    }
                    case ScreeningTypeConstants.JUMIO: {

                    }
                }
            }
            listString += screening_result;
            listString += ",";
            listString += screening_detail;
            csvDataReturn.add(listString);
        }
        return csvDataReturn;
    }

    private List<KYCSubmission> getAllKYCFormSubmissions(User user, String formId, HashMap<String, String> filters) {
        List<KYCSubmission> result = new ArrayList<>();// submissionService.findKYCSubmissionByFormId(formId, user,
                                                       // filters);
        return result;
    }

    private List<ScreeningSubmission> getAllScreeningFormSubmissions(User user, String formId,
            HashMap<String, String> filters) {
        List<ScreeningSubmission> result = new ArrayList<>();// submissionService.findScreeningSubmissionByFormId(formId,
                                                             // user, filters);
        return result;
    }
}
