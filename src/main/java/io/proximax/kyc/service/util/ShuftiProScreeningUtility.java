package io.proximax.kyc.service.util;

import com.google.common.base.Strings;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.domain.screening.ShuftiProScreening;
import org.json.simple.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ShuftiProScreeningUtility extends ScreeningUtility {


    // TODO Move to DTO
    public static ShuftiProScreening createScreeningData(Form form, JSONObject data) {
        HashMap hashMap = ScreeningUtility.getScreeningData(form, data);
        return new ShuftiProScreening(hashMap);
    }

    public static JSONObject parseToJSONObject(ShuftiProScreening screening) {
        HashMap<String, Object> jsonBody = new HashMap<>();

        if (!Strings.isNullOrEmpty(screening.getCallBackUrl()))
            jsonBody.put("callback_url", screening.getCallBackUrl());

        if (!Strings.isNullOrEmpty(screening.getRedirectUrl()))
            jsonBody.put("redirect_url", screening.getRedirectUrl());

        if (!Strings.isNullOrEmpty(screening.getEmail()))
            jsonBody.put("email", screening.getEmail());

        if (!Strings.isNullOrEmpty(screening.getCountry()))
            jsonBody.put("country", screening.getCountry());

        if (!Strings.isNullOrEmpty(screening.getLanguage()))
            jsonBody.put("language", screening.getLanguage());

//        int randomNum = ThreadLocalRandom.current().nextInt(1000, 1000000000 + 1);
        jsonBody.put("reference", "PRXKYC" + Instant.now().toEpochMilli());
        jsonBody.put("verification_mode","any");

        if (!Strings.isNullOrEmpty(screening.getFaceImageData()))
            jsonBody.put("face", getFaceData(screening));
        jsonBody.put("background_checks", getBackgroundData(screening));
        if (!Strings.isNullOrEmpty(screening.getFrontSideImageData()))
            jsonBody.put("document", getDocumentData(screening));
        if (screening.getAddressImage() != null)
            jsonBody.put("address", getAddressData(screening));

        return new JSONObject(jsonBody);
    }

    private static JSONObject getFaceData(ShuftiProScreening screening){
        HashMap<String, String> hashMap = new HashMap<>();

        if (!Strings.isNullOrEmpty(screening.getFaceImageData()))
            hashMap.put("proof", screening.getFaceImageData());
        return new JSONObject(hashMap);
    }

    private static JSONObject getBackgroundData(ShuftiProScreening screening){
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("name", getNameObject(screening));

        if (!Strings.isNullOrEmpty(screening.getDob()))
            hashMap.put("dob", screening.getDob());

        return new JSONObject(hashMap);
    }

    private static JSONObject getDocumentData(ShuftiProScreening screening) {
        HashMap<String, Object> hashMap = new HashMap<>();

        if (!Strings.isNullOrEmpty(screening.getFrontSideImageData()))
            hashMap.put("proof", screening.getFrontSideImageData());
        if (!Strings.isNullOrEmpty(screening.getBackSideImageData()))
            hashMap.put("additional_proof", screening.getBackSideImageData());

        ArrayList<String> supportedType = new ArrayList<>();
        supportedType.add("passport");
        hashMap.put("supported_types", supportedType);

        hashMap.put("name", getNameObject(screening));

        if (!Strings.isNullOrEmpty(screening.getDob()))
            hashMap.put("dob", screening.getDob());

        if (!Strings.isNullOrEmpty(screening.getIdNumber()))
            hashMap.put("document_number", screening.getIdNumber());

        return new JSONObject(hashMap);
    }

    private static JSONObject getAddressData(ShuftiProScreening screening) {
        HashMap<String, Object> hashMap = new HashMap<>();

        if (!Strings.isNullOrEmpty(screening.getAddressImageData()))
            hashMap.put("proof", screening.getAddressImageData());

        if (!Strings.isNullOrEmpty(screening.getFullAddress()))
            hashMap.put("full_address", screening.getFullAddress());

        hashMap.put("name", getNameObject(screening));

        ArrayList<String> supportedType = new ArrayList<>();
        supportedType.add("id_card");
        supportedType.add("bank_statement");
        hashMap.put("supported_types",supportedType);

        return new JSONObject(hashMap);
    }

    private static JSONObject getNameObject(ShuftiProScreening screening) {
        HashMap<String, Object> hashMap = new HashMap<>();

        if (!Strings.isNullOrEmpty(screening.getFirstName()))
            hashMap.put("first_name", screening.getFirstName());
        if (!Strings.isNullOrEmpty(screening.getLastName()))
            hashMap.put("last_name", screening.getLastName());
        if (!Strings.isNullOrEmpty(screening.getMiddleName()))
            hashMap.put("middle_name", screening.getMiddleName());

        return new JSONObject(hashMap);
    }
}
