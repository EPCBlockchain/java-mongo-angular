package io.proximax.kyc.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.constants.FormTypeConstants;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.service.DataService;
import io.proximax.kyc.service.FormService;
import io.proximax.kyc.service.UserService;
import io.proximax.kyc.service.storage.StorageService;
import io.proximax.kyc.service.util.FormUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DataResource {
    private final Logger log = LoggerFactory.getLogger(DataResource.class);
    private final FormService formService;
    private final UserService userService;
    private final DataService dataService;
    private final StorageService storageService;
    private final FormUtil formUtil;

    public DataResource(FormService formService, DataService dataService, UserService userService,
            StorageService storageService, FormUtil formUtil) {
        this.formService = formService;
        this.userService = userService;
        this.dataService = dataService;
        this.storageService = storageService;
        this.formUtil = formUtil;
    }

    @GetMapping("/form-submission/{formId}/export")
    @Timed
    public ArrayList<String> exportSubmittedForm(HttpServletRequest request,
            @PathVariable(value = "formId", required = true) String formId,
            @RequestParam(name = "filters", required = false) String filters) { //
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        Form form = formService.findOne(formId).get();
        HashMap<String, String> jsonFilters = this.formUtil.convertToHashMapFilters(filters);
        if (form.getTags().contains(FormTypeConstants.SCREENING)) {
            return dataService.exportScreeningFormSubmissions(user, formId, jsonFilters);
        } else {
            return dataService.exportKYCFormSubmissions(user, formId, jsonFilters);
        }
    }

    @GetMapping("/data/image/{hash}")
    @Timed
    public String getImageFromHash(@PathVariable(value = "hash", required = true) String hash) {
        return storageService.get(hash);
        // return null;
    }

    @GetMapping("/data/file/{hash}")
    @Timed
    public ResponseEntity<byte[]> getFileFromHash(@PathVariable(value = "hash", required = true) String hash) {

        String hashFile = storageService.get(hash);

        int start = hashFile.indexOf(":");
        int end = hashFile.indexOf(";");
        int fileType = hashFile.indexOf("/");
        int base64Start = hashFile.indexOf(",");

        if (start >= 0 && end >= 0 && ( fileType > start && fileType < end)) {
            byte[] bytes = Base64.decodeBase64(hashFile.substring(base64Start + 1));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", hashFile.substring(start + 1, end));
            return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
        }
        return null;
    }
}
