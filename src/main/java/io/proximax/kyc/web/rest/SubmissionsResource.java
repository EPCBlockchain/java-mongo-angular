package io.proximax.kyc.web.rest;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.codahale.metrics.annotation.Timed;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.constants.FormStatusConstants;
import io.proximax.kyc.domain.constants.FormTypeConstants;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.domain.mongo.ScreeningSubmission;
import io.proximax.kyc.security.AuthoritiesConstants;
import io.proximax.kyc.service.FormService;
import io.proximax.kyc.service.OrganizationService;
import io.proximax.kyc.service.SubmissionService;
import io.proximax.kyc.service.UserService;
import io.proximax.kyc.service.dto.KYCSubmissionDTO;
import io.proximax.kyc.service.dto.ScreeningSubmissionDTO;
import io.proximax.kyc.service.dto.SubmissionGridDTO;
import io.proximax.kyc.service.mail.MailService;
import io.proximax.kyc.service.util.FormUtil;
import io.proximax.kyc.web.rest.util.PaginationUtil;


@RestController
@RequestMapping("/api/submissions")
public class SubmissionsResource {

    private final Logger log = LoggerFactory.getLogger(SubmissionsResource.class);

    private final FormService formService;
    private final SubmissionService submissionService;
    private final UserService userService;
    private final MailService mailService;
    private final OrganizationService organizationService;
    private final FormUtil formUtil;
    private final ApplicationProperties applicationProperties;

    public SubmissionsResource(FormService formService, SubmissionService submissionService, OrganizationService organizationService,
                               FormUtil formUtil, MailService mailService, UserService userService, ApplicationProperties applicationProperties) {
        this.formService = formService;
        this.submissionService = submissionService;
        this.userService = userService;
        this.mailService = mailService;
        this.organizationService = organizationService;
        this.formUtil = formUtil;
        this.applicationProperties = applicationProperties;
    }


    @PostMapping("/request/{formId}/send")
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ADMIN })
    public void sendCustomerRequest(HttpServletRequest request, @PathVariable(value = "formId") String formId, @RequestBody String email) {
        Form form = formService.findOne(formId).get();
        Organization org = organizationService.findOne(form.getOrganizationId()).get();
        mailService.sendFormRequest(form.getId(), org.getName(), form.getOrganizationId() , email);
    }

    @GetMapping("/request/{orgId}/{formId}")
    @Timed
    @Secured({AuthoritiesConstants.USER})
    public KYCSubmissionDTO getRequestForm(HttpServletRequest request, @PathVariable(value = "orgId") String orgId, @PathVariable(value = "formId") String formId) {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        Optional<Form> form = formService.findOne(formId);
        KYCSubmissionDTO submission = submissionService.requestForm(orgId, formId, user);
        submission.setForm(form.get());
        return submission;
    }

    @PostMapping("/request/{submissionId}")
    @Timed
    @Secured({AuthoritiesConstants.USER})
    public void createCustomerData(HttpServletRequest request, @Valid @RequestBody KYCSubmissionDTO submission,
                                   @PathVariable(value = "submissionId") String submissionId) throws URISyntaxException {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        submission.setId(submissionId);
        submissionService.requestFormSubmit(user, submission);
    }

    @PostMapping("/return/{submissionId}")
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ADMIN })
    public void resendToCustomer(HttpServletRequest request, @PathVariable(value = "submissionId") String submissionId,
                                 @RequestBody String note) {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        KYCSubmissionDTO dto = submissionService.createResubmission(submissionId, user, note);
        if (dto.getUserId() != null) {
            Optional<User> customer = userService.getUserWithAuthorities(dto.getUserId());
            customer.ifPresent(value -> mailService.sendFormResubmitRequest(dto.getId(), "", value.getEmail(), note));
            Organization organization = organizationService.findOne(user.getOrganizationId()).get();
            this.formUtil.postBack(
                organization.getSecurity().getPostBackUrl(),
                dto.getFormId(),
                dto.getUserId(),
                submissionId,
                FormStatusConstants.RESEND,
                null,
                null
            );
        }
    }

    private ResponseEntity<SubmissionGridDTO> getKYCFormSubmissions(String formId, Pageable page, HashMap<String, String> filters) {
        SubmissionGridDTO<KYCSubmissionDTO> grid = new SubmissionGridDTO<>();
        Page<KYCSubmission> result = submissionService.findKYCSubmissionByFormId(formId, page, filters, grid);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, "/api/submissions/form/"+ formId);
        return new ResponseEntity<>(grid, headers, HttpStatus.OK);
    }

    private ResponseEntity<SubmissionGridDTO> getScreeningFormSubmissions(String formId, Pageable page, HashMap<String, String> filters) {
        SubmissionGridDTO<ScreeningSubmissionDTO> grid = new SubmissionGridDTO<>();
        Page<ScreeningSubmission> result = submissionService.findScreeningSubmissionByFormId(formId, page, filters, grid);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, "/api/submissions/form/"+ formId);
        return new ResponseEntity<>(grid, headers, HttpStatus.OK);
    }


    @GetMapping("/user/{submissionId}")
    @Timed
    @Secured({ AuthoritiesConstants.USER })
    public KYCSubmissionDTO getUserKYCSubmission(HttpServletRequest request,
                                                                   @PathVariable(value = "submissionId") String submissionId) {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        return submissionService.findKYCSubmission(submissionId, user);
    }

    @GetMapping("/user/form/{formId}/latest")
    @Timed
    @Secured({ AuthoritiesConstants.USER })
    public KYCSubmissionDTO getLatestSubmissionByFormId(HttpServletRequest request,
                                                 @PathVariable(value = "formId") String formId) {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        return submissionService.findLatestKYCSubmissionByFormId(formId, user);
    }

    @GetMapping("/kyc/{submissionId}")
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ADMIN })
    public KYCSubmissionDTO getKYCSubmission(HttpServletRequest request,
                                @PathVariable(value = "submissionId") String submissionId) {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        return submissionService.findKYCSubmission(submissionId, user);
    }

    // Create screening submission
    @GetMapping("/screening/{submissionId}/create")
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ADMIN })
    public ScreeningSubmissionDTO createScreeningSubmission(HttpServletRequest request,
                                                         @PathVariable(value = "submissionId") String submissionId) {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        return submissionService.createScreeningSubmission(submissionId, user);
    }

    // Create screening
    @PostMapping("/screening")
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ADMIN })
    public ResponseEntity<ScreeningSubmissionDTO> submitScreening(HttpServletRequest request, @Valid @RequestBody ScreeningSubmissionDTO submission){
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        submissionService.submitScreening(user, submission);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @PostMapping("/external/{formId}")
    @Timed
    public ResponseEntity<KYCSubmissionDTO> submitExternalSubmission(HttpServletRequest request,
                                @PathVariable(value = "formId") String formId,
                                @RequestParam(value = "uniqueId", required = false) String uniqueId,
                                @RequestParam(value = "validationId", required = false) String validationId,
                                @Valid @RequestBody KYCSubmissionDTO submission){
        Form form = formService.findOne(formId).get();
        Organization organization = organizationService.findOne(form.getOrganizationId()).get();

        if (organizationService.validateRequest(organization, request)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            this.formUtil.requestUniqueIdValidation(organization.getSecurity().getValidationUrl(), uniqueId, validationId);
        } catch (HttpClientErrorException ex) {
            return new ResponseEntity<>(HttpStatus.valueOf(ex.getRawStatusCode()));
        }

        submission.setUserId(uniqueId);
        KYCSubmissionDTO retSubmission = submissionService.submitExternalSubmission(formId, submission);

        // send post back
        this.formUtil.postBack(
            organization.getSecurity().getPostBackUrl(),
            formId,
            uniqueId,
            retSubmission.getId(),
            retSubmission.getStatus(),
            validationId,
            null
        );
        return new ResponseEntity<>(retSubmission, HttpStatus.OK);
    }

    @PostMapping("/external/postbackcfg/{formId}")
    @Timed
    public HttpStatus updatePostbackUrl(@PathVariable(value = "formId") String formId,
                                  @RequestParam(value = "postback_url") String postback_url,
                                  @RequestParam(name = "apiKey") String apiKey){

        Optional<Form> form = formService.findOne(formId);

        if(form.isPresent()){
            Form aForm = form.get();
            Optional<Organization> organization = organizationService.findOne(aForm.getOrganizationId());
            if(organization.isPresent()){
                if(apiKey.equalsIgnoreCase(organization.get().getSecurity().getApiKey())){
                    aForm.setPostBackURL(postback_url);
                    formService.save(aForm);
                    return HttpStatus.OK;
                }else{
                    return HttpStatus.UNAUTHORIZED;
                }
            }else{
                return HttpStatus.NOT_FOUND;
            }
        }
        return HttpStatus.NOT_FOUND;
    }

    // with apikey
    @GetMapping("/external/{formId}")
    @Timed
    public ResponseEntity<SubmissionGridDTO> getExternalSubmissions(@PathVariable(value = "formId") String formId, Pageable page, @RequestParam(name = "filters", required = false) String filters,
                                                                    @RequestParam(name = "apiKey", required = false) String apiKey, HttpServletRequest request) {
        if (apiKey != null) {
            String appAPIKey = applicationProperties.getSecurity().getApiKey();
            if (!appAPIKey.equals(apiKey)) {
                Optional<Organization> userOrganization = organizationService.findOneByApiKey(apiKey);
                Boolean isPresent = userOrganization.isPresent();
                if (!isPresent) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
        } else if (request.getRemoteUser() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        HashMap<String, String> jsonFilters = this.formUtil.convertToHashMapFilters(filters);
        log.debug(filters);
        Page<KYCSubmissionDTO> result = submissionService.findExternalSubmissions(formId, page, jsonFilters, apiKey);
        if(result!=null){
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, "/api/external/" + formId);
            SubmissionGridDTO<KYCSubmissionDTO> grid = new SubmissionGridDTO(result.getContent());
            return new ResponseEntity<>(grid, headers, HttpStatus.OK);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }

    // Get user submissions with apikey
    @GetMapping("/user")
    @Timed
    public ResponseEntity<SubmissionGridDTO> getUserSubmissions(HttpServletRequest request, @RequestParam(name = "filters", required = false) String filters, @RequestParam(value = "apiKey", required = false) String apiKey,
                                                                @RequestParam(value = "status", defaultValue = "SUBMITTED") String status, Pageable page, @RequestParam(value = "uniqueId", required = false) String uniqueId) {
        if (apiKey != null) {
            String appAPIKey = applicationProperties.getSecurity().getApiKey();
            if (!appAPIKey.equals(apiKey)) {
                Optional<Organization> userOrganization = organizationService.findOneByApiKey(apiKey);
                Boolean isPresent = userOrganization.isPresent();
                if (!isPresent) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
                // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else if (request.getRemoteUser() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser());

        String userId = uniqueId;
        if (user.isPresent()) userId = user.get().getId();
        else if (userId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<String> statusList = new ArrayList<>();
        statusList.add(status);
        if (status.equals(FormStatusConstants.SUBMITTED)) {
            statusList.add(FormStatusConstants.RESEND);
            statusList.add(FormStatusConstants.APPROVED);
            statusList.add(FormStatusConstants.REJECTED);
            statusList.add(FormStatusConstants.FURTHER_APPROVAL);
            statusList.add(FormStatusConstants.ESCALATED);
        }

        Page<KYCSubmissionDTO> result = submissionService.findKYCSubmissionsByUserAndStatusIn(userId, statusList, filters, false, page);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, "/api/user/pending");
        SubmissionGridDTO<KYCSubmissionDTO> grid = new SubmissionGridDTO(result.getContent());
        return new ResponseEntity<>(grid, headers, HttpStatus.OK);
    }

    // with apikey
    @GetMapping("/external/form-data/{formId}")
    @Timed
    public ResponseEntity<KYCSubmissionDTO> getExternalSubmissions(@PathVariable(value = "formId") String formId,
                                                                   @RequestParam(name = "apikey", required = false) String apiKey,
                                                   @RequestParam(name = "uniqueId", required = false) String uniqueId,
                                                   @RequestParam(name = "validationId", required = false) String validationId) throws RestClientException {

        Form form = this.formService.findOne(formId).get();
        Organization organization = this.organizationService.findOne(form.getOrganizationId()).get();

        log.debug("{}", applicationProperties.getForm().getValidationURL());
        if (applicationProperties.getForm().getValidationURL() != null) {
            try {
                this.formUtil.requestUniqueIdValidation(organization.getSecurity().getValidationUrl(), uniqueId, validationId);
            } catch (HttpClientErrorException ex) {
                return new ResponseEntity<>(HttpStatus.valueOf(ex.getRawStatusCode()));
            }
        }


        KYCSubmissionDTO result = new KYCSubmissionDTO();

        if (uniqueId != null) {
            result = submissionService.findExternalSubmissionByFormIdAndUserId(formId, uniqueId);
        }
        result.setForm(form);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // with apikey
    @GetMapping("/external/{formId}/{submissionId}")
    @Timed
    public KYCSubmissionDTO getExternalSubmission( @PathVariable(value = "submissionId") String submissionId,
                                                                    @PathVariable(value = "formId") String formId) {
        return submissionService.findExternalSubmission(formId, submissionId, "");
    }

    // with api key
    @GetMapping("/form/{formId}")
    @Timed
    @Secured({AuthoritiesConstants.OBTEAM , AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.ADMIN })
    public ResponseEntity<SubmissionGridDTO> getFormSubmissions(HttpServletRequest request, @PathVariable (value = "formId", required = true) String formId,
                                                                Pageable page, @RequestParam(name = "filters", required = false) String filters) {

        log.info("\n\n\n\n\n\n\n ------------------------------------------------ getFormSubmissions (formId)  ----------------------------------------------------------------\n" + formId + "\n\n\n\n\n\n\n\n\n\n\n\n" );
        
        Form form = formService.findOne(formId).get();
        
        log.info("\n\n\n\n\n\n\n ------------------------------------------------ filters  ----------------------------------------------------------------\n" + filters + "\n\n\n\n\n\n\n\n\n\n\n\n" );
        
        
        
        HashMap<String, String> jsonFilters = this.formUtil.convertToHashMapFilters(filters);
        if (form.getTags().contains(FormTypeConstants.SCREENING)) {
            return getScreeningFormSubmissions(formId, page, jsonFilters);
        } else {
            return getKYCFormSubmissions(formId, page, jsonFilters);
        }
    }

    // with api key
    @GetMapping("/formall/{formId}")
    @Timed
    @Secured({AuthoritiesConstants.OBTEAM , AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.ADMIN })
    public ResponseEntity<SubmissionGridDTO> getFormSubmissionsAll(HttpServletRequest request, @PathVariable (value = "formId", required = true) String formId,
                                                                Pageable page, @RequestParam(name = "filters", required = false) String filters) {
        //aqui victor
        log.info("\n----------------------------------------------------------\n\t" + "/formall/{formId}\n\t" );


        Form form = formService.findOne(formId).get();
        HashMap<String, String> jsonFilters = this.formUtil.convertToHashMapFilters(filters);
        if (form.getTags().contains(FormTypeConstants.SCREENING)) {
            return getScreeningFormSubmissions(formId, page, jsonFilters);
        } else {
            return getKYCFormSubmissions(formId, page, jsonFilters);
        }
    }



    // with apikey
    // Get screening submission
    @GetMapping("/screening/{submissionId}")
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ADMIN })
    public ScreeningSubmissionDTO getScreeningSubmission(HttpServletRequest request, @PathVariable(value = "submissionId") String submissionId) {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        return submissionService.findScreeningSubmission(submissionId, user);
    }

    // with apikey
    @GetMapping("/external/{formid}/unique/{uniqueid}")
    @Timed
    public ResponseEntity<List<KYCSubmissionDTO>> getExternalSubmissionByUniqueId(@PathVariable(value = "uniqueid") String uniqueid,
                                                                    @PathVariable(value = "formid") String formId,
                                                                    @RequestParam(name = "filters", required = false) String filters,
                                                                    @RequestParam(name = "apikey") String apiKey) {

        if (apiKey != null) {
            String appAPIKey = applicationProperties.getSecurity().getApiKey();
            log.debug("{} {}", appAPIKey, apiKey);
            if (!appAPIKey.equals(apiKey)) {
                Optional<Organization> userOrganization = organizationService.findOneByApiKey(apiKey);
                if (!userOrganization.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
        }

        HashMap<String, String> jsonFilters = this.formUtil.convertToHashMapFilters(filters);
        List<KYCSubmissionDTO> response = submissionService.findExternalSubmissionByUniqueId(uniqueid, formId, jsonFilters, apiKey);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/test/external/postback")
    @Timed
    public ResponseEntity<Object> externalPostBackTest(@RequestBody() JSONObject request) {
        ArrayList credentialId = (ArrayList<String>) request.get("credential");
        ArrayList uniqueId = (ArrayList<String>) request.get("uniqueId");
        log.debug("credential id : {}", credentialId);
        log.debug("uniqueId id : {}", uniqueId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/test/external/validate")
    @Timed
    public ResponseEntity<Object> externalValidateTest(@RequestBody() JSONObject request) {
        ArrayList validationId = (ArrayList<String>) request.get("validationId");
        ArrayList uniqueId = (ArrayList<String>) request.get("uniqueId");
        if (validationId.get(0) == null) {
            return new ResponseEntity<>("invalid validation id", HttpStatus.UNAUTHORIZED);
        }
        if (uniqueId.get(0) == null) {
            return new ResponseEntity<>("invalid unique id", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(request.get("validationId"), HttpStatus.OK);
    }
}
