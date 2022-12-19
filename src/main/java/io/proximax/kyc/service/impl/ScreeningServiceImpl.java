package io.proximax.kyc.service.impl;

import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.constants.ScreeningTypeConstants;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.domain.mongo.ScreeningSubmission;
import io.proximax.kyc.domain.screening.ShuftiProScreening;
import io.proximax.kyc.domain.screening.keys.ShuftiProKeyPair;
import io.proximax.kyc.repository.FormRepository;
import io.proximax.kyc.repository.KYCSubmissionRepository;
import io.proximax.kyc.repository.ScreeningSubmissionRepository;
import io.proximax.kyc.service.OrganizationService;
import io.proximax.kyc.service.ScreeningService;
import io.proximax.kyc.service.storage.StorageService;
import io.proximax.kyc.service.util.EncryptionUtil;
import io.proximax.kyc.service.util.FormUtil;
import io.proximax.kyc.service.util.ShuftiProScreeningUtility;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final Logger log = LoggerFactory.getLogger(ScreeningServiceImpl.class);

    private final KYCSubmissionRepository kycSubmissionRepository;
    private final StorageService storageService;
    private final FormRepository formRepository;
    private final ApplicationProperties appConfig;
    private final ScreeningSubmissionRepository screeningSubmissionRepository;
    private final OrganizationService organizationService;
    private final EncryptionUtil encriptionUtility;
    private final FormUtil formUtil;

    public ScreeningServiceImpl(ApplicationProperties appConfig, KYCSubmissionRepository kycSubmissionRepository,
            FormRepository formRepository, ScreeningSubmissionRepository screeningSubmissionRepository,
            StorageService storageService, FormUtil formUtil, EncryptionUtil encryptionUtil,
            OrganizationService organizationService) {
        this.kycSubmissionRepository = kycSubmissionRepository;
        this.screeningSubmissionRepository = screeningSubmissionRepository;
        this.formRepository = formRepository;
        this.storageService = storageService;
        this.appConfig = appConfig;
        this.organizationService = organizationService;
        this.encriptionUtility = encryptionUtil;
        this.formUtil = formUtil;
    }

    public JSONObject initiateShuftiProScreening(String submissionId, User user) {
        Optional<KYCSubmission> dbSubmission = kycSubmissionRepository.findById(submissionId);
        if (dbSubmission.isPresent()) {
            KYCSubmission submission = dbSubmission.get();
            Form form = formRepository.findById(submission.getFormId()).get();
            String ipfsHash = storageService.get(submission.getDataHash());
            JSONObject data = this.encriptionUtility.decryptToJSONObject(ipfsHash);
            List<JSONObject> fileList = this.formUtil.findFileInputs(form);

            JSONObject encryptImageData = storageService.getFile(fileList, data);

            ShuftiProScreening screening = ShuftiProScreeningUtility.createScreeningData(form, encryptImageData);
            Optional<Organization> organization = organizationService.findOne(user.getOrganizationId());
            ShuftiProKeyPair keyPair = organization.get().getScreeningKeys().getShuftiPro();
            keyPair.setClientId(this.encriptionUtility.decrypt(keyPair.getClientId()));
            keyPair.setSecretKey(this.encriptionUtility.decrypt(keyPair.getSecretKey()));
            JSONObject screeningResult = this.requestShuftiProScreening(screening, user, keyPair);
            Optional<ScreeningSubmission> dbScreeningSubmission = screeningSubmissionRepository
                    .findBySubmissionReferenceId(submissionId);
            if (screeningResult != null && dbScreeningSubmission.isPresent()) {
                ScreeningSubmission screeningSubmission = dbScreeningSubmission.get();
                String screeningResultHash = encriptionUtility.encryptJSONObject(screeningResult);
                screeningSubmission.setScreeningDataHash(screeningResultHash);
                screeningSubmission.setScreeningDate(Instant.now());
                screeningSubmission.setScreeningType(ScreeningTypeConstants.SHUFTI_PRO);
                screeningSubmissionRepository.save(screeningSubmission);
            }
            return screeningResult;
        }
        return null;
    }

    public JSONObject requestIDMScreening(String submissionId, User user) {
        return null;
    }

    public JSONObject requestThomsonReutersScreening(String submissionId, User user) {
        return null;
    }

    public JSONObject requestJumioScreening(String submissionId, User user) {
        return null;
    }

    private JSONObject requestShuftiProScreening(ShuftiProScreening screening, User user, ShuftiProKeyPair keyPair) {
        HttpHeaders headers = new HttpHeaders();
        String orgId = user.getOrganizationId();
        String clientId = keyPair.getClientId();
        String secret = keyPair.getSecretKey();
        if (clientId.isEmpty() || secret.isEmpty())
            return null;
        String auth = Base64Utils.encodeToString(String.format("%s:%s", clientId, secret).getBytes());
        headers.add(HttpHeaders.AUTHORIZATION, String.format("Basic %s", auth));
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        screening.setCallbackUrl("https://kyc-app.proximax.io/");
        screening.setRedirectUrl("https://kyc-app.proximax.io/");
        screening.setLanguage("en");
        JSONObject jsonBody = ShuftiProScreeningUtility.parseToJSONObject(screening);

        log.debug(jsonBody.toString());

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody.toString(), headers);
        String serviceUrl = appConfig.getShuftiPro().getUrl();
        RestTemplate newRestTemplate = new RestTemplate();
        String jsonString = "";
        try {
            jsonString = newRestTemplate.postForObject(serviceUrl, requestEntity, String.class);
            jsonString = jsonString.replace("verification.accepted", "Accepted");
            jsonString = jsonString.replace("verification.declined", "Declined");
            jsonString = jsonString.replace("Document", "ID");
            jsonString = jsonString.replace("document", "id");
            jsonString = jsonString.replace(":1", ":\"Accepted\"");
            jsonString = jsonString.replace(":0", ":\"Declined\"");
        } catch (RestClientException ex) {
            log.debug("ex {}: ", ex.toString());
        }
        try {
            return (JSONObject) new JSONParser().parse(jsonString);
        } catch (Exception ex) {
            return null;
        }
    }

}
