package io.proximax.kyc.service.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.service.storage.IpfsService;

@Service
public class StorageService {

    private final AmazonClient amazonClient;
    private final ApplicationProperties applicationProperties;
    private final IpfsService ipfsService;

    public StorageService(AmazonClient amazonClient, ApplicationProperties applicationProperties,
            IpfsService ipfsService) {
        this.amazonClient = amazonClient;
        this.applicationProperties = applicationProperties;
        this.ipfsService = ipfsService;
    }

    public String store(String base64String) {
        String uuid = UUID.randomUUID().toString();

        switch (applicationProperties.getStorageSelection()) {
            case "ipfs":
                return this.ipfsService.addTextFile(uuid, base64String);
            case "aws-s3":
                amazonClient.upload(uuid, base64String);
                return uuid;
        }
        return null;
    }

    public String get(String key) {
        switch (applicationProperties.getStorageSelection()) {
            case "ipfs":
                return this.ipfsService.getFile(key);
            case "aws-s3":
                return amazonClient.get(key);
        }
        return null;
    }

    public JSONObject addFiles(List<JSONObject> fileList, JSONObject data) {
        if (!fileList.isEmpty()) {
            for (JSONObject file : fileList) {
                String fileKey = (String) file.get("key");
                if (data.containsKey(fileKey)) {
                    ArrayList temp = (ArrayList) data.get(fileKey);
                    JSONArray fileImage = newIterator(temp);
                    try {
                        String imageDataHash = this.store(fileImage.toJSONString());
                        JSONObject hashBody = new JSONObject();
                        hashBody.put("hashUrl", imageDataHash);
                        data.replace(fileKey, hashBody);
                    } catch (Exception ex) {
                        return null;
                    }
                }
            }
        }
        return data;
    }

    public JSONObject getFile(List<JSONObject> fileList, JSONObject data) {
        JSONParser parse = new JSONParser();
        for (JSONObject file : fileList) {
            String fileKey = (String) file.get("key");
            if (data.containsKey(fileKey)) {
                JSONObject bodyHash = (JSONObject) data.get(fileKey);
                try {
                    String jsonString = this.get(bodyHash.get("hashUrl").toString());
                    JSONArray fileImage = (JSONArray) parse.parse(jsonString);
                    data.replace(fileKey, fileImage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return data;
    }

    private static JSONArray newIterator(ArrayList components) {
        JSONArray componentSet = new JSONArray();
        Iterator<Object> iterator = components.iterator();
        while (iterator.hasNext()) {
            componentSet.add(iterator.next());
        }
        return componentSet;
    }

}
