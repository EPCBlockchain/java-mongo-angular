package io.proximax.kyc.service.util;

import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.xpx.utils.CryptoUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EncryptionUtil {
    private char[] key;
    private final static Logger log = LoggerFactory.getLogger(EncryptionUtil.class);

    public EncryptionUtil(ApplicationProperties applicationProperties) {
        this.key = applicationProperties.getSecurity().getKey().toCharArray();
    }

    public static String encypt(String payload, char[] key) {
        try {
            return CryptoUtils.encryptToBase64String(payload.getBytes(), key);
        } catch(Exception ex) {
            log.debug(ex.getMessage());
            return null;
        }
    }

    public static String decrypt(String payload, char[] key) {
        try {
            return CryptoUtils.decryptToBase64String(payload, key);
        } catch(Exception ex) {
            return null;
        }
    }

    public String decrypt(String payload) {
        return EncryptionUtil.decrypt(payload, this.key);
    }

    public List<String> decryptList(List<String> payload) {
        List<String> list = new ArrayList<>();
        payload.forEach(hash -> {
            try {
                list.add(CryptoUtils.decryptToBase64String(hash, this.key));
            } catch(Exception ex) { }
        });
        return list;
    }

    public JSONObject decryptToJSONObject(String payload) {
        try {
            String jsonString = EncryptionUtil.decrypt(payload, this.key);
            try{
                return (JSONObject) new JSONParser().parse(jsonString);
            }catch(ParseException ex){
                log.debug("JSON.parse Failed {}", ex.getMessage());
                return null;
            }
        } catch(Exception ex) {
            log.debug("Decryption Failed : {}", ex.getMessage());
            return null;
        }
    }

    public String encrypt(String payload) {
        return EncryptionUtil.encypt(payload, this.key);
    }

    public String encryptJSONObject(JSONObject objectPayload) {
        try {
            return EncryptionUtil.encypt(objectPayload.toJSONString(), this.key);
        } catch(Exception ex) {
            log.debug(ex.getMessage());
            return null;
        }
    }
}
