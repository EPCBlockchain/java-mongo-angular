package io.proximax.kyc.service.storage;

import io.proximax.kyc.config.ApplicationProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.ipfs.api.IPFS;
import java.io.IOException;
import io.ipfs.api.NamedStreamable;
import io.ipfs.api.NamedStreamable.FileWrapper;
import io.ipfs.multihash.Multihash;
import java.util.UUID;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.parser.JSONParser;

@Service
public class IpfsService {

    IPFS ipfs;

    public IpfsService(ApplicationProperties appConfig) {
        if (appConfig.getStorageSelection().equals("ipfs")) {
            ipfs = new IPFS(appConfig.getIPFS().getHost());
        }
    }

    public String getRefsLocal() {
        String res = "";
        try {
            res = ipfs.refs.local().toString();
        } catch (IOException ex) {
            // log.debug("{}", ex);
        }
        return res;
    }

    public String getSwarmPeers() {
        String res = "";
        try {
            res = ipfs.swarm.peers().toString();
        } catch (IOException ex) {
            // log.debug("{}", ex);
        }
        return res;
    }

    public String getFile(String hash) {
        String res = "";
        try {
            Multihash filePointer = Multihash.fromBase58(hash);
            res = new String(ipfs.cat(filePointer));
        } catch (IOException ex) {
//            log.debug("{}", ex);
        }
        return res;
    }

    public String addTextFile(String fileName, String data) {
        try {
            NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(fileName, data.getBytes());
            Multihash hash = ipfs.add(file).get(0).hash;
            ipfs.pin.add(hash);
            return hash.toString();
        } catch (IOException ex) {
            // log.debug("{}", ex);
        }
        return null;
    }

    public JSONObject addImageFile(List<JSONObject> fileList, JSONObject data) {
        if (!fileList.isEmpty()) {
            for (JSONObject file : fileList) {
                String fileKey = (String) file.get("key");
                if (data.containsKey(fileKey)) {
                    ArrayList temp = (ArrayList) data.get(fileKey);
                    JSONArray fileImage = newIterator(temp);
                    try {
                        String imageDataHash = addTextFile(UUID.randomUUID().toString(), fileImage.toJSONString());
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

    public JSONObject getImageFile(List<JSONObject> fileList, JSONObject data) {
        JSONParser parse = new JSONParser();
        for (JSONObject file : fileList) {
            String fileKey = (String) file.get("key");
            if (data.containsKey(fileKey)) {
                JSONObject bodyHash = (JSONObject) data.get(fileKey);
                try {
                    String jsonString = getFile(bodyHash.get("hashUrl").toString());
                    JSONArray fileImage = (JSONArray) parse.parse(jsonString);
                    data.replace(fileKey, fileImage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // log.debug("getImageFile issue with addressImage");
                }
            }
        }
        return data;
    }

    public String addFile(FileWrapper file) {
        try {
            Multihash hash = ipfs.add(file).get(0).hash;
            ipfs.pin.add(hash);
            return hash.toString();
        } catch (IOException ex) {
            // log.debug("{}", ex);
        }
        return null;
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
