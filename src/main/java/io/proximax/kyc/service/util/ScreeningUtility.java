package io.proximax.kyc.service.util;

import io.proximax.kyc.domain.mongo.Form;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ScreeningUtility {

    public static HashMap getScreeningData(Form form, JSONObject submission) {
        HashMap<String, String> data = new HashMap<>();        
        ScreeningUtility.findScreeningValues(form.getComponents(), submission, data);
        return data;
    }

    private static void findScreeningValues(JSONArray components, HashMap reference, HashMap screeningData) {       
        Iterator<JSONObject> iterator = components.iterator();
        while (iterator.hasNext()) {
            JSONObject component = new JSONObject(iterator.next());
            if (component.containsKey("components")) {
                ScreeningUtility.findScreeningValues(ScreeningUtility.newIterator((ArrayList) component.get("components")), reference, screeningData);
            }
            if (component.containsKey("columns")) {
                ScreeningUtility.findScreeningValues(ScreeningUtility.newIterator((ArrayList) component.get("columns")), reference, screeningData);
            }
            if (component.containsKey("properties")) {
                LinkedHashMap properties = (LinkedHashMap) component.get("properties");
                if (properties.containsKey("screening")) {
                    String referenceKey = component.get("key").toString();
                    Object referenceValue = reference.get(referenceKey);
                    if(referenceValue != null)
                        screeningData.put(properties.get("screening"), referenceValue);                  
                }
            }
        }
    }

    private static JSONArray newIterator(ArrayList components) {
        JSONArray componentSet = new JSONArray();
        Iterator<Object> iterator = components.iterator();
        while (iterator.hasNext()) { componentSet.add(iterator.next()); }
        return componentSet;
    }

}
