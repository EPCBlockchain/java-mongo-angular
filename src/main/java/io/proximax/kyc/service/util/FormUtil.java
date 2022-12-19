package io.proximax.kyc.service.util;

import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.logging.log4j.util.Strings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.globalsettings.GlobalSettings;
import io.proximax.kyc.domain.keypairs.SubmissionColumnKeyPair;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.service.GlobalSettingsService;
import io.proximax.kyc.service.dto.KYCSubmissionDTO;
import io.proximax.kyc.service.dto.ScreeningSubmissionDTO;

@Service
public class FormUtil {
    private final ApplicationProperties applicationProperties;
    private final GlobalSettingsService globalSettingsService;
    private EncryptionUtil encryptionUtil;
    private final  Logger log = LoggerFactory.getLogger(FormUtil.class);

    public FormUtil(EncryptionUtil encryptionUtil, ApplicationProperties applicationProperties, GlobalSettingsService globalSettingsService) {
        this.encryptionUtil = encryptionUtil;
        this.applicationProperties = applicationProperties;
        this.globalSettingsService = globalSettingsService;
    }

    public List<String> createHashedFilters(HashMap<String, String> filters, boolean full) {
        List<String> hashFilters = new ArrayList<String>();
        try {
            if (filters != null) {
                filters.keySet().forEach(key -> {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(key, filters.get(key).toLowerCase().replaceAll("\\s+", Strings.EMPTY));
                    String filterHash = this.encryptionUtil.encryptJSONObject(new JSONObject(map));
                    hashFilters.add(filterHash);
                });
                if (full) {
                    hashFilters.add(this.encryptionUtil.encryptJSONObject(new JSONObject(filters)));
                }
            }
        } catch(Exception ex) {
            return null;
        }
        return hashFilters;
    }

    public JSONObject convertFilterHashToData(List<String> hashedFilters) {
        log.debug("hashedFilters : " + hashedFilters.size());
        HashMap<String, String> filterData = new HashMap<>();
        String stringData = this.encryptionUtil.decrypt(hashedFilters.get(hashedFilters.size() - 1));
        JSONObject data;
        try{
            data = (JSONObject) new JSONParser().parse(stringData);
            data.keySet().forEach(key -> filterData.put(key.toString(), data.get(key).toString()));
        } catch (ParseException ex){ }
        return new JSONObject(filterData);
    }

    public HashMap<String, String> convertToHashMapFilters(String filters) {
        if (!com.google.common.base.Strings.isNullOrEmpty(filters)) {
            try {
                return new Gson().fromJson(filters, new TypeToken<HashMap<String, String>>(){}.getType());
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    public  HashMap<String, String> generateFilter(Form form, KYCSubmissionDTO submission) {
        HashMap<String, String> filter = new HashMap<>();
        this.getTableViews(form).forEach(component -> {
            if (submission.getData().containsKey(component.get("key"))) {
                filter.put(component.get("key").toString(), submission.getData().get(component.get("key")).toString().toLowerCase());
            }
        });
        return filter;
    }

    public  HashMap<String, String> generateFilter(Form form, ScreeningSubmissionDTO submission) {
        HashMap<String, String> filter = new HashMap<>();
        this.getTableViews(form).forEach(component -> {
            if (submission.getData().containsKey(component.get("key"))) {
                filter.put(component.get("key").toString(), submission.getData().get(component.get("key")).toString().toLowerCase());
            }
        });
        return filter;
    }

    public  List<SubmissionColumnKeyPair> createTableColumns(Form form) {
        return this.getTableViews(form).stream().map(map -> {
            SubmissionColumnKeyPair keypair = new SubmissionColumnKeyPair();
            keypair.setKey(map.get("key").toString());
            keypair.setLabel(map.get("label").toString());
            return keypair;
        }).collect(Collectors.toList());
    }

    private  List<JSONObject> getTableViews(Form form) {
        List<JSONObject> keys = new ArrayList<>();
        this.searchTableView(form.getComponents(), keys);
        return keys;
    }

    private  void searchTableView(JSONArray components, List<JSONObject> keys) {
        Iterator<JSONObject> iterator = components.iterator();
        while (iterator.hasNext()) {
            JSONObject component = new JSONObject(iterator.next());
            if (component.containsKey("components")) {
                this.searchTableView(this.newIterator((ArrayList) component.get("components")), keys);
            }
            if (component.containsKey("columns")) {
                this.searchTableView(this.newIterator((ArrayList) component.get("columns")), keys);
            }

            if (!component.containsKey("columns") && !component.containsKey("components") && component.containsKey("tableView") ) {
                if (!component.get("type").equals("button") && component.get("tableView").equals(true) && component.get("input").equals(true)) {
                    keys.add(component);
                }
            }
        }
    }

    private  JSONArray newIterator(ArrayList components) {
        JSONArray componentSet = new JSONArray();
        Iterator<Object> iterator = components.iterator();
        while (iterator.hasNext()) { componentSet.add(iterator.next()); }
        return componentSet;
    }

    public  List<JSONObject> findFileInputs(Form form) {
        List<JSONObject> fileInputs = new ArrayList<>();
        this.findFileInputs(form.getComponents(), fileInputs);
        return fileInputs;
    }

    private  void findFileInputs(JSONArray components, List<JSONObject> list) {
        Iterator<JSONObject> iterator = components.iterator();
        while (iterator.hasNext()) {
            JSONObject component = new JSONObject(iterator.next());
            if (component.containsKey("components")) {
                this.findFileInputs(this.newIterator((ArrayList) component.get("components")), list);
            }
            if (component.containsKey("columns")) {
                this.findFileInputs(this.newIterator((ArrayList) component.get("columns")), list);
            }
            Object type = component.get("type");
            if(type != null){
                if (type.equals("file")) {
                    list.add(component);
                }
            }
        }
    }

    public  JSONObject createDefaultValuesFromReference(Form form, JSONObject reference) {
        JSONObject defaultValues = new JSONObject();
        this.findDefaultValues(form.getComponents(), reference, defaultValues);
        return defaultValues;
    }

    private  void findDefaultValues(JSONArray components, JSONObject reference, JSONObject defaultValues) {
        Iterator<JSONObject> iterator = components.iterator();
        while (iterator.hasNext()) {
            JSONObject component = new JSONObject(iterator.next());
            if (component.containsKey("components")) {
                this.findDefaultValues(this.newIterator((ArrayList) component.get("components")), reference, defaultValues);
            }
            if (component.containsKey("columns")) {
                this.findDefaultValues(this.newIterator((ArrayList) component.get("columns")), reference, defaultValues);
            }
            if (component.containsKey("properties")) {
                LinkedHashMap properties = (LinkedHashMap) component.get("properties");
                if (properties.containsKey("referenceId")) {
                    String referenceKey = properties.get("referenceId").toString();
                    Object referenceValue = reference.get(referenceKey);
                    if (referenceValue != null) {
                        defaultValues.put(component.get("key"), referenceValue);
                    }
                }
            }
        }
    }

    public void findLabelAndValues(JSONArray components, HashMap reference, ArrayList labels , ArrayList values) {
        Iterator<JSONObject> iterator = components.iterator();
        String key ="";
        String lable = "";
        while (iterator.hasNext()) {
            JSONObject component = new JSONObject(iterator.next());
            if (component.containsKey("components")) {
                this.findLabelAndValues(this.newIterator((ArrayList) component.get("components")), reference, labels, values);
            }
            if (component.containsKey("columns")) {
                this.findLabelAndValues(this.newIterator((ArrayList) component.get("columns")), reference, labels, values);
            }
            if (component.containsKey("type")) {
                Object obj = component.get("type");
                if(obj != null){
                    String type = (String)obj;
                    if(type.equalsIgnoreCase("file") || type.equalsIgnoreCase("panel") ||
                        type.equalsIgnoreCase("selectboxes") || type.equalsIgnoreCase("htmlelement") ||
                        type.equalsIgnoreCase("textarea") || type.equalsIgnoreCase("button") ||
                        type.equalsIgnoreCase("well")|| type.equalsIgnoreCase("columns") ){

                    }else{
                        Object properties = component.get("properties");
                        if (properties!=null){
                            key = (String)component.get("key");
                            lable = (String)component.get("label");

                            Object objVal =  reference.get(key);
                            labels.add(lable);
                            if(objVal!= null){
                                values.add(objVal.toString());
                            }
                        }
                    }
                }
            }
        }
    }


    public List<String> findCredentials(Form kycForm) {
        return this.getTableViews(kycForm).stream()
                .filter(map -> map.containsKey("properties"))
                .map(map -> map.get("key").toString())
                .collect(Collectors.toList());
    }

    public void postBack(String postBackURL, String formId, String uniqueId, String submissionId, String status, String validationId, Object credential) throws RestClientException {
        GlobalSettings settings = this.globalSettingsService.getSettings();
        String url = this.applicationProperties.getForm().getPostBackURL();
        if (settings.getSecuritySettings() != null) {
            if (settings.getSecuritySettings().getPostBackUrl() != null) {
                url = settings.getSecuritySettings().getPostBackUrl();
            }
        }
        if (postBackURL != null && !postBackURL.isEmpty()) {
            url = postBackURL;
        }
        if (url.isEmpty()) { return; }

        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        map.add("formId", formId);
        map.add("uniqueId", uniqueId);
        map.add("submissionId", submissionId);
        map.add("status" , status);
        if (credential != null) {
            map.add("credential", credential);
        }
        if (validationId != null)
            map.add("validationId", validationId);

        this.createPostRequest(url, map);
    }

    public void requestUniqueIdValidation(String validationURL, String uniqueId, String validationId) throws RestClientException {
        GlobalSettings settings = this.globalSettingsService.getSettings();
        String url = this.applicationProperties.getForm().getValidationURL();
        if (settings.getSecuritySettings() != null) {
            if (settings.getSecuritySettings().getValidationUrl() != null) {
                url = settings.getSecuritySettings().getValidationUrl();
            }
        }
        if (validationURL != null && !validationURL.isEmpty()) {
            url = validationURL;
        }
        if (url.isEmpty()) { return; }
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        map.add("uniqueId", uniqueId);
        map.add("validationId", validationId);
        this.createPostRequest(url, map);
    }

    private void createPostRequest(String url, MultiValueMap<String, Object> map) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        RestTemplate newRestTemplate = new RestTemplate();
        String response = newRestTemplate.postForObject(url, requestEntity, String.class);
        log.debug(response);
    }

}
