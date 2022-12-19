package io.proximax.kyc.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.Credential;
import io.proximax.kyc.service.CredentialService;
import jdk.nashorn.internal.runtime.options.Option;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.OrganizationEmailRecipient;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.constants.FormStatusConstants;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.domain.mongo.ScreeningSubmission;
import io.proximax.kyc.repository.FormRepository;
import io.proximax.kyc.repository.KYCSubmissionRepository;
import io.proximax.kyc.repository.ScreeningSubmissionRepository;
import io.proximax.kyc.repository.UserRepository;
import io.proximax.kyc.service.OrganizationService;
import io.proximax.kyc.service.SubmissionService;
import io.proximax.kyc.service.dto.KYCSubmissionDTO;
import io.proximax.kyc.service.dto.ScreeningSubmissionDTO;
import io.proximax.kyc.service.dto.SubmissionGridDTO;
import io.proximax.kyc.service.mail.MailService;
import io.proximax.kyc.service.storage.StorageService;
import io.proximax.kyc.service.util.EncryptionUtil;
import io.proximax.kyc.service.util.FormUtil;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final Logger log = LoggerFactory.getLogger(SubmissionServiceImpl.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
            .withZone(ZoneId.systemDefault());

    private final KYCSubmissionRepository kycSubmissionRepository;
    private final ScreeningSubmissionRepository screeningSubmissionRepository;
    private final FormRepository formRepository;
    private final OrganizationService organizationService;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final EncryptionUtil encryptionUtil;
    private final FormUtil formUtil;
    private final StorageService storageService;
    private final CredentialService credentialService;
    private final ApplicationProperties applicationProperties;

    public SubmissionServiceImpl(KYCSubmissionRepository kycSubmissionRepository, FormRepository formRepository,
            OrganizationService organizationService, ScreeningSubmissionRepository screeningSubmissionRepository,
            EncryptionUtil encryptionUtil, FormUtil formUtil, StorageService storageService,
            ApplicationProperties applicationProperties, CredentialService credentialService,
            UserRepository userRepository, MailService mailService) {
        this.kycSubmissionRepository = kycSubmissionRepository;
        this.formRepository = formRepository;
        this.organizationService = organizationService;
        this.screeningSubmissionRepository = screeningSubmissionRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.encryptionUtil = encryptionUtil;
        this.storageService = storageService;
        this.formUtil = formUtil;
        this.credentialService = credentialService;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Long countByForm(Form form) {
        if (form.getTags().contains("customer")) {
            return kycSubmissionRepository.countByFormId(form.getId());
        } else {
            return screeningSubmissionRepository.countByFormId(form.getId());
        }
    }

    // KYC Submissions

    @Override
    public KYCSubmissionDTO requestForm(String orgId, String formId, User user) {
        KYCSubmission submission = kycSubmissionRepository.findOneByUserIdAndFormIdAndStatusNot(user.getId(), formId,
                FormStatusConstants.RESEND);
        KYCSubmissionDTO submissionDTO;
        if (submission == null) {
            submission = new KYCSubmission();

            submission.setUserId(user.getId());
            submission.setFormId(formId);
            submission.setCreationDate(Instant.now());
            submission.setStatus(FormStatusConstants.DRAFT);

            kycSubmissionRepository.save(submission);

            submissionDTO = new KYCSubmissionDTO(submission, false);
        } else {
            submissionDTO = new KYCSubmissionDTO(submission, false);
            if (!submissionDTO.getStatus().equals(FormStatusConstants.DRAFT)) {

                String dataHash = storageService.get(submission.getDataHash());
                JSONObject submissionHashData = this.encryptionUtil.decryptToJSONObject(dataHash);
                submissionDTO.setData(submissionHashData);
            }
        }
        return submissionDTO;
    }

    @Override
    public KYCSubmissionDTO findKYCSubmission(String submissionId, User user) {
        KYCSubmission submission = kycSubmissionRepository.findById(submissionId).get();
        Form form = formRepository.findById(submission.getFormId()).get();
        KYCSubmissionDTO submissionDTO = new KYCSubmissionDTO(submission);
        if (!Strings.isNullOrEmpty(submission.getDataHash())) {
            String dataHash = storageService.get(submission.getDataHash());
            JSONObject submissionHashData = this.encryptionUtil.decryptToJSONObject(dataHash);
            submissionDTO.setData(submissionHashData);
        }
        submissionDTO.setForm(form);
        return submissionDTO;
    }

    @Override
    public KYCSubmissionDTO findLatestKYCSubmissionByFormId(String formId, User user) {
        List<KYCSubmission> kycSubmissions = kycSubmissionRepository.findByFormIdAndUserId(formId, user.getId());
        if (!kycSubmissions.isEmpty()) {
            KYCSubmission kycSubmission = kycSubmissions.get(kycSubmissions.size() - 1);
            KYCSubmissionDTO submissionDTO = new KYCSubmissionDTO(kycSubmission);
            if (!Strings.isNullOrEmpty(kycSubmission.getDataHash())) {
                String dataHash = storageService.get(kycSubmission.getDataHash());
                JSONObject submissionHashData = this.encryptionUtil.decryptToJSONObject(dataHash);
                submissionDTO.setData(submissionHashData);
            }
            Optional<ScreeningSubmission> screeningSubmission = screeningSubmissionRepository
                    .findBySubmissionReferenceId(submissionDTO.getId());
            screeningSubmission.ifPresent(submission -> submissionDTO.setRemarks(submission.getRemarks()));
            return submissionDTO;
        }
        return null;
    }

    @Override
    public ScreeningSubmissionDTO createScreeningSubmission(String referenceId, User user) {
        KYCSubmission submission = kycSubmissionRepository.findById(referenceId).get();
        Form referenceForm = formRepository.findById(submission.getFormId()).get();
        Form screeningForm = formRepository.findById(referenceForm.getScreeningFormId()).get();
        Optional<ScreeningSubmission> dbScreeningSubmission = screeningSubmissionRepository
                .findBySubmissionReferenceId(submission.getId());
        ScreeningSubmissionDTO submissionDTO;
        if (dbScreeningSubmission.isPresent()) {
            ScreeningSubmission dbSubmission = dbScreeningSubmission.get();
            submissionDTO = new ScreeningSubmissionDTO(dbSubmission);
            String dataHash = storageService.get(dbSubmission.getDataHash());
            JSONObject data = this.encryptionUtil.decryptToJSONObject(dataHash);
            submissionDTO.setData(data);

            if (dbSubmission.getScreeningDataHash() != null) {
                String screeningDataHash = dbSubmission.getScreeningDataHash();
                JSONObject screeningData = this.encryptionUtil.decryptToJSONObject(screeningDataHash);
                submissionDTO.setScreeningData(screeningData);
            }

            Optional<User> lastUpdate = userRepository.findById(dbSubmission.getLastUpdatedBy());
            if (lastUpdate.isPresent()) {
                submissionDTO.setLastUpdatedBy(lastUpdate.get().getFirstName() + " " + lastUpdate.get().getLastName());
            }
            submissionDTO.setLastUpdatedBy(user.getFirstName() + " " + user.getLastName());
        } else {
            ScreeningSubmission newScreening = new ScreeningSubmission();

            newScreening.setOrganizationId(user.getOrganizationId());
            newScreening.setSubmissionReferenceId(referenceId);

            String dataHash = storageService.get(submission.getDataHash());
            JSONObject referenceData = this.encryptionUtil.decryptToJSONObject(dataHash);
            JSONObject submissionDefault = this.formUtil.createDefaultValuesFromReference(screeningForm, referenceData);
            String screeningDataHash = storageService.store(this.encryptionUtil.encryptJSONObject(submissionDefault));

            newScreening.setUserId(user.getId());
            newScreening.setStatus(FormStatusConstants.PENDING_APPROVAL);
            newScreening.setFormId(screeningForm.getId());
            newScreening.setDataHash(screeningDataHash);
            newScreening.setScreeningDataHash(screeningDataHash);
            newScreening.setCreationDate(Instant.now());
            newScreening.setLastUpdate(Instant.now());
            newScreening.setLastUpdateBy(user.getId());
            // newScreening.setDataApi("---------------------------------");

            screeningSubmissionRepository.save(newScreening);
            submissionDTO = new ScreeningSubmissionDTO(newScreening);
            submissionDTO.setData(submissionDefault);

            HashMap<String, String> filters = this.formUtil.generateFilter(screeningForm, submissionDTO);

            filters.put("id", newScreening.getId());
            filters.put("status", newScreening.getStatus());
            filters.put("lastUpdate", formatter.format(newScreening.getLastUpdate()));
            filters.put("creationDate", formatter.format(newScreening.getCreationDate()));

            List<String> filterHashes = this.formUtil.createHashedFilters(filters, true);
            newScreening.setFilterHash(filterHashes);
            screeningSubmissionRepository.save(newScreening);
            submissionDTO.setLastUpdatedBy(user.getFirstName() + " " + user.getLastName());
        }
        submissionDTO.setForm(screeningForm);
        return submissionDTO;
    }

    @Override
    public ScreeningSubmissionDTO findScreeningSubmission(String submissionId, User user) {
        ScreeningSubmission screeningSubmission = screeningSubmissionRepository.findById(submissionId).get();

        // log.info("\n\n\n\n\n\n\n\n\n\n ScreeningSubmissionDTO findScreeningSubmission - getDataApi:  \n\n\n---------- " + screeningSubmission.getDataApi() + " ---------- \n\n\n\n\n\n\n\n\n\n"   );
        
        Form screeningForm = formRepository.findById(screeningSubmission.getFormId()).get();
        ScreeningSubmissionDTO submissionDTO = new ScreeningSubmissionDTO(screeningSubmission);
        submissionDTO.setForm(screeningForm);

        Optional<User> lastUpdate = userRepository.findById(screeningSubmission.getLastUpdatedBy());

        String dataHash = storageService.get(screeningSubmission.getDataHash());
        JSONObject data = this.encryptionUtil.decryptToJSONObject(dataHash);
        if (screeningSubmission.getScreeningDataHash() != null) {
            String screeningDataHash = screeningSubmission.getScreeningDataHash();
            JSONObject screeningData = this.encryptionUtil.decryptToJSONObject(screeningDataHash);
            submissionDTO.setScreeningData(screeningData);
        }

        // log.info("\n\n\n\n\n\n\n\n\n\n data  \n\n\n---------- " + screeningSubmission.getDataFormio() + " ---------- \n\n\n\n\n\n\n\n\n\n"   );

        submissionDTO.setData(data);
        submissionDTO.setForm(screeningForm);
        if (lastUpdate.isPresent()) {
            submissionDTO.setLastUpdatedBy(lastUpdate.get().getFirstName() + " " + lastUpdate.get().getLastName());
        }
        // aqui 
        submissionDTO.setDataApi(screeningSubmission.getDataApi());
        submissionDTO.setDataFormio(screeningSubmission.getDataFormio());
        return submissionDTO;
        
    }

    @Override
    public void requestFormSubmit(User user, KYCSubmissionDTO submission) {
        Optional<KYCSubmission> dbKYCSubmission = kycSubmissionRepository.findById(submission.getId());

        if (dbKYCSubmission.isPresent()) {
            KYCSubmission kycSubmission = dbKYCSubmission.get();
            kycSubmission.setStatus(FormStatusConstants.SUBMITTED);
            kycSubmission.setLastUpdate(Instant.now());
            Optional<Form> form = formRepository.findById(dbKYCSubmission.get().getFormId());
            /* Get list of file component on form */
            List<JSONObject> fileList = this.formUtil.findFileInputs(form.get());
            /* Base on list of file, search on user submision data, put them on IPFS */
            JSONObject ImageEncryptData = this.storageService.addFiles(fileList, submission.getData());

            String dataHash = this.encryptionUtil.encryptJSONObject(ImageEncryptData);
            String ipfsHash = storageService.store(dataHash);

            HashMap<String, String> filters = this.formUtil.generateFilter(form.get(), submission);

            filters.put("id", kycSubmission.getId());
            filters.put("status", kycSubmission.getStatus());
            filters.put("title", form.get().getName().toLowerCase());
            filters.put("lastUpdate", formatter.format(kycSubmission.getLastUpdate()));
            filters.put("creationDate", formatter.format(kycSubmission.getCreationDate()));
            log.debug("filters : " + filters.toString());

            List<String> filterHashes = this.formUtil.createHashedFilters(filters, true);

            kycSubmission.setFilterHash(filterHashes);
            kycSubmission.setDataHash(ipfsHash);

            kycSubmissionRepository.save(kycSubmission);
            Optional<Organization> organization = this.organizationService.findOne(form.get().getOrganizationId());

            List<OrganizationEmailRecipient> emailList = organization.get().getEmailRecipients();
            if (emailList.size() > 0) {
                List<String> emails = emailList.stream().map(recipient -> recipient.getEmail())
                        .collect(Collectors.toList());
                mailService.sendSubmissionReceivedEmail(String.join(",", emails), kycSubmission);
            }

            this.formUtil.postBack(organization.get().getSecurity().getPostBackUrl(), dbKYCSubmission.get().getFormId(),
                    kycSubmission.getUserId(), kycSubmission.getId(), submission.getStatus(), null, null);
        }
    }

    @Override
    public KYCSubmissionDTO createResubmission(String submissionId, User user, String note) {
        KYCSubmission dbKYCSubmission = kycSubmissionRepository.findById(submissionId).get();

        dbKYCSubmission.setStatus(FormStatusConstants.RESEND);
        dbKYCSubmission.setLastUpdate(Instant.now());

        KYCSubmissionDTO dbDTO = new KYCSubmissionDTO(dbKYCSubmission);
        String dataHash = storageService.get(dbKYCSubmission.getDataHash());
        JSONObject dbData = this.encryptionUtil.decryptToJSONObject(dataHash);
        dbDTO.setData(dbData);

        Optional<ScreeningSubmission> dbScreeningSubmission = screeningSubmissionRepository
                .findBySubmissionReferenceId(dbKYCSubmission.getId());

        Form form = formRepository.findById(dbKYCSubmission.getFormId()).get();


        HashMap<String, String> dbKYCFilters = this.formUtil.generateFilter(form, dbDTO);

        dbKYCFilters.put("id", dbKYCSubmission.getId());
        dbKYCFilters.put("status", dbKYCSubmission.getStatus());
        dbKYCFilters.put("title", form.getName().toLowerCase());
        dbKYCFilters.put("lastUpdate", formatter.format(dbKYCSubmission.getLastUpdate()));
        dbKYCFilters.put("creationDate", formatter.format(dbKYCSubmission.getCreationDate()));

        List<String> dbKYCFilterHashes = this.formUtil.createHashedFilters(dbKYCFilters, true);
        dbKYCSubmission.setFilterHash(dbKYCFilterHashes);

        kycSubmissionRepository.save(dbKYCSubmission);

        KYCSubmission submission = new KYCSubmission();

        submission.setUserId(dbKYCSubmission.getUserId());
        submission.setFormId(dbKYCSubmission.getFormId());
        submission.setCreationDate(Instant.now());
        submission.setLastUpdate(submission.getCreationDate());
        submission.setStatus(FormStatusConstants.DRAFT);
        submission.setDataHash(dbKYCSubmission.getDataHash());

        if (dbScreeningSubmission.isPresent()) {
            dbScreeningSubmission.get().setLastUpdate(Instant.now());
            dbScreeningSubmission.get().setStatus(FormStatusConstants.RESEND);
            dbScreeningSubmission.get().setLastUpdateBy(user.getId());
            dbScreeningSubmission.get().setRemarks(note);
            screeningSubmissionRepository.save(dbScreeningSubmission.get());
        }

        // create new hashes

        HashMap<String, String> filters = this.formUtil.generateFilter(form, dbDTO);

        filters.put("id", submission.getId());
        filters.put("status", submission.getStatus());
        filters.put("title", form.getName().toLowerCase());
        filters.put("lastUpdate", formatter.format(submission.getLastUpdate()));
        filters.put("creationDate", formatter.format(submission.getCreationDate()));

        try {
            List<String> filterHashes = this.formUtil.createHashedFilters(filters, true);
            submission.setFilterHash(filterHashes);
        } catch (Exception ex) {
            return null;
        }

        kycSubmissionRepository.save(submission);
        return new KYCSubmissionDTO(submission);
    }

    @Override
    public Page<KYCSubmissionDTO> findKYCSubmissionsByUserAndStatusIn(String userId, List<String> status,
            String filters, boolean withData, Pageable page) {
        HashMap<String, String> jsonFilters = this.formUtil.convertToHashMapFilters(filters);
        List<String> hashedFilters = this.formUtil.createHashedFilters(jsonFilters, false);
        Page<KYCSubmission> pages;
        if (filters == null) {
            pages = kycSubmissionRepository.findAllByUserIdAndStatusInOrderByCreationDateDesc(userId, status, page);
        } else {
            pages = kycSubmissionRepository
                    .findAllKYCSubmissionsByUserIdAndFilterHashesInAndStatusInOrderByCreationDateDesc(userId,
                            hashedFilters, status, page);
        }
        return pages.map(submission -> {
            KYCSubmissionDTO submissionDTO = new KYCSubmissionDTO(submission);
            Optional<Form> form;
            if (withData) {
                form = formRepository.findById(submission.getFormId());
            } else {
                form = formRepository.findTitleById(submission.getFormId());
            }
            if (form.isPresent()) {
                submissionDTO.setForm(form.get());
            }
            submissionDTO.setId(submission.getId());
            submissionDTO.setCreationDate(submission.getCreationDate());
            submissionDTO.setStatus(submission.getStatus());
            submissionDTO.setUserId(submission.getUserId());
            submissionDTO.setIsExternal(submission.getIsExternal());

            return submissionDTO;
        });
    }

    @Override
    public Page<KYCSubmission> findKYCSubmissionByFormId(String formId, Pageable page, HashMap<String, String> filters,
            SubmissionGridDTO<KYCSubmissionDTO> gridOut) {
        List<String> hashedFilters = this.formUtil.createHashedFilters(filters, false);
        Form form = formRepository.findById(formId).get();

        Page<KYCSubmission> submissions;
        List<String> status = new ArrayList<>();
        status.add(FormStatusConstants.DRAFT);
        
        
        if (filters != null) {
            submissions = kycSubmissionRepository.findAllByFormIdAndFilterHashesAndStatusNotInOrderByCreationDateDesc(
                    formId, hashedFilters, status, page);
        } else {
            submissions = kycSubmissionRepository.findAllByFormIdAndStatusNotInOrderByCreationDateDesc(formId, status,
                    page);
        }
        // submissions = kycSubmissionRepository.findAllByFormIdAndStatusNotInOrderByCreationDateDesc(formId, status, page);

        gridOut.setColumns(this.formUtil.createTableColumns(form));
        log.debug("start loop");
        log.debug(Instant.now().toString());
        submissions.stream().forEach(map -> {
            KYCSubmissionDTO submission = new KYCSubmissionDTO(map, true);
            // Set filters as submission data for grid rows
            JSONObject submissionHashData = this.formUtil.convertFilterHashToData(map.getFilterHashes());
            submission.setData(submissionHashData);
            gridOut.getRows().add(submission);
        });
        log.debug(Instant.now().toString());
        form.setComponents(null);
        gridOut.getRequestData().put("form", form);
        gridOut.getRequestData().put("filters", filters);
        return submissions;
    }

    @Override
    public Page<ScreeningSubmission> findScreeningSubmissionByFormId(String formId, Pageable page,
            HashMap<String, String> filters, SubmissionGridDTO<ScreeningSubmissionDTO> gridOut) {
        List<String> hashedFilters = this.formUtil.createHashedFilters(filters, false);
        Page<ScreeningSubmission> submissions;
 
        if (filters != null && !hashedFilters.isEmpty()) {
            submissions = screeningSubmissionRepository.findByFormIdAndFilterHashesInOrderByCreationDateDesc(formId,
                    hashedFilters, page);
        } else {
            submissions = screeningSubmissionRepository.findByFormIdOrderByCreationDateDesc(formId, page);
        }

        Form form = formRepository.findById(formId).get();
        gridOut.setColumns(this.formUtil.createTableColumns(form));

        List<String> userIds = submissions.stream().map(submission -> submission.getLastUpdatedBy())
                .collect(Collectors.toList());

        List<User> userList = userRepository.findByIdIn(userIds);

        // Problem : The static forms to feed the API, cause collision with convertFilterHashToData that is used by the dynamic forms of the application. 
        // Solution: For each static form id, the form id that this function receives is validated, so that it does not take the validation of convertFilterHashToData.  
        submissions.stream().forEach(map -> {
            ScreeningSubmissionDTO submission = new ScreeningSubmissionDTO(map);
            
            // Form static: Edlx & Mall Nuestro
            // if ( formId != "615467de527bf300012631c9" && formId != "6153a4f4527bf300012631c8" ) {
            if ( !"615467de527bf300012631c9".equals(formId) && !"6153a4f4527bf300012631c8".equals(formId) ) { 
                JSONObject submissionHashData = this.formUtil.convertFilterHashToData(map.getFilterHashes());
                submission.setData(submissionHashData);
            }
            // log.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" );
            
            Optional<User> updateUser = userList.stream()
                    .filter(user -> user.getId().equals(submission.getLastUpdatedBy())).findFirst();
            if (updateUser.isPresent()) {
                submission.setLastUpdatedBy(updateUser.get().getFirstName() + " " + updateUser.get().getLastName());
            }
            gridOut.getRows().add(submission);
        });

        form.setComponents(null);
        gridOut.getRequestData().put("form", form);
        return submissions;
    }

    @Override
    public void submitScreening(User user, ScreeningSubmissionDTO submission) {
        Optional<ScreeningSubmission> dbScreeningSubmission = screeningSubmissionRepository
                .findById(submission.getId());

        if (dbScreeningSubmission.isPresent()) {
            ScreeningSubmission screeningSubmission = dbScreeningSubmission.get();
            screeningSubmission.setStatus(submission.getStatus());
            screeningSubmission.setLastUpdate(Instant.now());
            screeningSubmission.setRemarks(submission.getRemarks());
            screeningSubmission.setLastUpdateBy(user.getId());

            String dataHash = this.encryptionUtil.encryptJSONObject(submission.getData());
            String ipfsHash = storageService.store(dataHash);

            Form form = formRepository.findById(submission.getFormId()).get();

            HashMap<String, String> filters = this.formUtil.generateFilter(form, submission);

            filters.put("id", screeningSubmission.getId());
            filters.put("status", screeningSubmission.getStatus());
            filters.put("lastUpdate", formatter.format(screeningSubmission.getLastUpdate()));
            filters.put("creationDate", formatter.format(screeningSubmission.getCreationDate()));

            List<String> filterHashes = this.formUtil.createHashedFilters(filters, true);

            screeningSubmission.setDataHash(ipfsHash);
            screeningSubmission.setFilterHash(filterHashes);

            screeningSubmissionRepository.save(screeningSubmission);


            KYCSubmission kycSubmission = this.kycSubmissionRepository.findById(submission.getSubmissionReferenceId()).get();

            kycSubmission.setStatus(screeningSubmission.getStatus());
            kycSubmission.setLastUpdate(Instant.now());


            KYCSubmissionDTO kycSubmissionDTO = new KYCSubmissionDTO(kycSubmission);

            final String kycDataHashFile = storageService.get(kycSubmission.getDataHash());
            kycSubmissionDTO.setData(this.encryptionUtil.decryptToJSONObject(kycDataHashFile));

            final HashMap<String, String> kycSubmissionFilters = this.formUtil.generateFilter(form, kycSubmissionDTO);

            kycSubmissionFilters.put("id", kycSubmissionDTO.getId());
            kycSubmissionFilters.put("status", kycSubmissionDTO.getStatus());
            kycSubmissionFilters.put("lastUpdate", formatter.format(kycSubmissionDTO.getLastUpdate()));
            kycSubmissionFilters.put("creationDate", formatter.format(kycSubmissionDTO.getCreationDate()));

            final List<String> kycFilterHashes = this.formUtil.createHashedFilters(kycSubmissionFilters, true);
            log.debug("this is filterhashes {}", kycFilterHashes);
            kycSubmission.setFilterHash(kycFilterHashes);

            String credentialId = null;
            Credential credential = null;

            if (screeningSubmission.getStatus().equals(FormStatusConstants.APPROVED)) {
                Form kycForm = formRepository.findById(kycSubmission.getFormId()).get();
                credentialId = kycForm.getCredentialId();
                kycSubmission.setCredentialId(credentialId);

               if (credentialId != null) {
                   Optional<Credential> dbCredential = credentialService.findOne(credentialId);

                   if (dbCredential.isPresent()) {
                       credential = dbCredential.get();
                       credential.setImage(this.applicationProperties.getBaseHost() + "/api/data/file/" + credential.getImage());
                       credential.setStatus(null);
                       credential.setCreationDate(null);

                       String kycDataHash = storageService.get(kycSubmission.getDataHash());
                       JSONObject referenceData = this.encryptionUtil.decryptToJSONObject(dataHash);

                       Map<String, String> map = new HashMap<>();
                       List<String> contents = this.formUtil.findCredentials(kycForm);
                       log.debug("contents:");
                       log.debug("contents: {}", contents);
                       contents.forEach(property -> {
                           if (referenceData.get(property) != null) {
                               map.put(property, referenceData.get(property).toString());
                           }
                       });
                       credential.setContents(map);
                   }
               }
            }

            submission.setLastUpdatedBy(user.getFirstName() + " " + user.getLastName());
            kycSubmissionRepository.save(kycSubmission);

            Organization organization = organizationService.findOne(form.getOrganizationId()).get();

            this.formUtil.postBack(
                organization.getSecurity().getPostBackUrl(),
                form.getId(), kycSubmission.getUserId(),
                kycSubmission.getId(),
                submission.getStatus(),
                null,
                credential
            );

            this.sendStatusEmail(screeningSubmission, kycSubmission);
        }
    }

    private void sendStatusEmail(ScreeningSubmission screeningSubmission, KYCSubmission kycSubmission) {
        Optional<Organization> organization = this.organizationService.findOne(screeningSubmission.getOrganizationId());

        if (!kycSubmission.getIsExternal()) {
            Optional<User> submissionUser = this.userRepository.findById(kycSubmission.getUserId());
            if (screeningSubmission.getStatus().equals(FormStatusConstants.APPROVED)) {
                mailService.sendApprovalEmail(submissionUser.get(), kycSubmission.getId());
            } else if (screeningSubmission.getStatus().equals(FormStatusConstants.REJECTED)) {
                mailService.sendRejectionEmail(submissionUser.get(), kycSubmission.getId(),
                        screeningSubmission.getRemarks());
            }
        }
        List<OrganizationEmailRecipient> emailList = organization.get().getEmailRecipients();
        if (screeningSubmission.getStatus().equals(FormStatusConstants.REJECTED)
                || screeningSubmission.getStatus().equals(FormStatusConstants.APPROVED)) {
            List<String> emails = emailList.stream().map(recipient -> recipient.getEmail())
                    .collect(Collectors.toList());
            if (!emailList.isEmpty())
                mailService.sendStatusEmail(String.join(",", emails), screeningSubmission);
        }
    }

    @Override
    public KYCSubmissionDTO submitExternalSubmission(String formId, KYCSubmissionDTO submissionDTO) {
        Form form = formRepository.findById(formId).get();
        KYCSubmission submission = new KYCSubmission();

        Instant today = Instant.now();
        submission.setIsExternal(true);
        submission.setFormId(formId);
        submission.setCreationDate(today);
        submission.setUserId(submissionDTO.getUserId());
        submission.setStatus(FormStatusConstants.SUBMITTED);
        submission.setUserId(submissionDTO.getUserId());
        submission.setLastUpdate(Instant.now());

        kycSubmissionRepository.save(submission);

        List<JSONObject> fileList = this.formUtil.findFileInputs(form);

        JSONObject ImageEncryptData = storageService.addFiles(fileList, submissionDTO.getData());
        String dataHash = this.encryptionUtil.encryptJSONObject(ImageEncryptData);

        // TODO: Test
        String ipfsHash = storageService.store(dataHash);

        HashMap<String, String> filters = this.formUtil.generateFilter(form, submissionDTO);

        filters.put("id", submission.getId());
        filters.put("status", submission.getStatus());
        filters.put("title", form.getName().toLowerCase());
        filters.put("lastUpdate", formatter.format(submission.getLastUpdate()));
        filters.put("creationDate", formatter.format(submission.getCreationDate()));

        List<String> filterHashes = this.formUtil.createHashedFilters(filters, true);

        submission.setFilterHash(filterHashes);
        submission.setDataHash(ipfsHash);

        kycSubmissionRepository.save(submission);

        Optional<Organization> organization = this.organizationService.findOne(form.getOrganizationId());
        List<OrganizationEmailRecipient> emailList = organization.get().getEmailRecipients();
        if (emailList.size() > 0) {
            List<String> emails = emailList.stream().map(recipient -> recipient.getEmail())
                    .collect(Collectors.toList());
            mailService.sendSubmissionReceivedEmail(String.join(",", emails), submission);
        }
        return new KYCSubmissionDTO(submission, false);
    }

    @Override
    public KYCSubmissionDTO findExternalSubmission(String formId, String submissionId, String apiKey) {
        KYCSubmission submission = kycSubmissionRepository.findById(submissionId).get();
        KYCSubmissionDTO submissionDTO = new KYCSubmissionDTO(submission);
        if (!Strings.isNullOrEmpty(submission.getDataHash())) {
            String dataHash = storageService.get(submission.getDataHash());
            JSONObject submissionHashData = this.encryptionUtil.decryptToJSONObject(dataHash);
            submissionDTO.setData(submissionHashData);
        }
        return submissionDTO;
    }

    @Override
    public KYCSubmissionDTO findExternalSubmissionByFormIdAndUserId(String formId, String uniqueId) {
        Optional<KYCSubmission> submission = kycSubmissionRepository
                .getTopByFormIdAndUserIdOrderByCreationDateDesc(formId, uniqueId);
        KYCSubmissionDTO submissionDTO = new KYCSubmissionDTO();
        if (submission.isPresent()) {
            submissionDTO = new KYCSubmissionDTO(submission.get());
            if (!Strings.isNullOrEmpty(submission.get().getDataHash())) {
                String dataHash = storageService.get(submission.get().getDataHash());
                JSONObject submissionHashData = this.encryptionUtil.decryptToJSONObject(dataHash);
                submissionDTO.setData(submissionHashData);
            }
        } else {
            submissionDTO.setStatus(FormStatusConstants.DRAFT);
        }
        return submissionDTO;
    }

    @Override
    public KYCSubmissionDTO findByUserIdAndCredentialId(String userId, String credentialId) {
        Optional<KYCSubmission> dbSubmission = this.kycSubmissionRepository.findByUserIdAndCredentialId(userId, credentialId);
        KYCSubmissionDTO submissionDTO = null;
        if (dbSubmission.isPresent()) {
            Optional<ScreeningSubmission> dbScreening = screeningSubmissionRepository.findBySubmissionReferenceId(dbSubmission.get().getId());

            submissionDTO = new KYCSubmissionDTO(dbSubmission.get());
            String dataHash = storageService.get(dbSubmission.get().getDataHash());
            JSONObject submissionHashData = this.encryptionUtil.decryptToJSONObject(dataHash);
            submissionDTO.setData(submissionHashData);
            submissionDTO.setRemarks(dbScreening.get().getRemarks());
        }
        return submissionDTO;
    }

    @Override
    public List<KYCSubmissionDTO> findExternalSubmissionByUniqueId(String uniqueId, String formId, HashMap filters,
            String apiKey) {
        Form form = formRepository.findById(formId).get();
        Optional<Organization> organization = organizationService.findOne(form.getOrganizationId());
        if (organization.isPresent()) {
            if (apiKey.equalsIgnoreCase(organization.get().getSecurity().getApiKey())) {
                List<String> hashedFilters = this.formUtil.createHashedFilters(filters, false);
                log.debug("hashedFilters {} : ", hashedFilters);
                List<KYCSubmission> submissions = null;
                if (hashedFilters.isEmpty()) {
                    submissions = kycSubmissionRepository.findAllByUserId(uniqueId);
                } else {
                    submissions = kycSubmissionRepository.findAllByUserIdAndFilterHashes(uniqueId, hashedFilters);
                }
                log.debug("submissions {} : ", submissions);
                return submissions.stream().map(map -> {
                    KYCSubmissionDTO submission = new KYCSubmissionDTO(map, true);
                    JSONObject submissionHashData = this.formUtil.convertFilterHashToData(map.getFilterHashes());
                    submission.setData(submissionHashData);
                    return submission;
                }).collect(Collectors.toList());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Page<KYCSubmissionDTO> findExternalSubmissions(String formId, Pageable page, HashMap filters,
            String apiKey) {
        Form form = formRepository.findById(formId).get();
        Optional<Organization> organization = organizationService.findOne(form.getOrganizationId());
        if (organization.isPresent()) {
            if (apiKey.equalsIgnoreCase(organization.get().getSecurity().getApiKey())) {
                List hashedFilters = this.formUtil.createHashedFilters(filters, true);
                Page<KYCSubmission> submissions;

                List<String> status = new ArrayList<>();
                status.add(FormStatusConstants.DRAFT);

                if (filters != null) {
                    submissions = kycSubmissionRepository
                            .findAllByFormIdAndFilterHashesAndStatusNotInOrderByCreationDateDesc(formId, hashedFilters,
                                    status, page);
                } else {
                    submissions = kycSubmissionRepository.findAllByFormIdAndStatusNotInOrderByCreationDateDesc(formId,
                            status, page);
                }
                return submissions.map(map -> {
                    KYCSubmissionDTO submission = new KYCSubmissionDTO(map, true);
                    JSONObject submissionHashData = this.formUtil.convertFilterHashToData(map.getFilterHashes());
                    submission.setData(submissionHashData);
                    return submission;
                });
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}
