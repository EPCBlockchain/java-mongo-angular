package io.proximax.kyc.service;

import io.proximax.kyc.domain.Authority;
import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.form.UserForm;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.domain.mongo.ScreeningSubmission;
import io.proximax.kyc.service.dto.KYCSubmissionDTO;
import io.proximax.kyc.service.dto.ScreeningSubmissionDTO;
import io.proximax.kyc.service.dto.SubmissionGridDTO;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface SubmissionService {
    Long countByForm(Form form);
//    Optional<KYCSubmission> findKYCSubmission(String submissionId);
    KYCSubmissionDTO findKYCSubmission(String submissionId, User user);
    KYCSubmissionDTO findLatestKYCSubmissionByFormId(String formId, User user);
    ScreeningSubmissionDTO createScreeningSubmission(String referenceId, User user);
    ScreeningSubmissionDTO findScreeningSubmission(String referenceId, User user);
    KYCSubmissionDTO requestForm(String orgId, String formId, User user);
    KYCSubmissionDTO createResubmission(String submissionId, User user, String note);

    void requestFormSubmit(User user, KYCSubmissionDTO submission);
    Page<KYCSubmissionDTO> findKYCSubmissionsByUserAndStatusIn(String userId, List<String> status, String filter,  boolean withData, Pageable page);
    // Page<KYCSubmission> findKYCSubmissionByFormIdAndStatusIn(String formId, List<String> status, User user, Pageable page,
    //                                                          HashMap<String, String> filters, SubmissionGridDTO<KYCSubmissionDTO> gridOut);

    void submitScreening(User user, ScreeningSubmissionDTO submission);

    Page<KYCSubmission> findKYCSubmissionByFormId(String formId, Pageable page, HashMap<String, String> filters, SubmissionGridDTO<KYCSubmissionDTO> gridOut);
    Page<ScreeningSubmission> findScreeningSubmissionByFormId(String formId, Pageable page, HashMap<String, String> filters, SubmissionGridDTO<ScreeningSubmissionDTO> gridOut);

    KYCSubmissionDTO submitExternalSubmission(String formId, KYCSubmissionDTO submission);
    KYCSubmissionDTO findExternalSubmission(String formId, String submissionId, String apiKey);
    List<KYCSubmissionDTO> findExternalSubmissionByUniqueId(String uniqueId, String formId, HashMap filters, String apiKey);
    Page<KYCSubmissionDTO> findExternalSubmissions(String formId, Pageable page, HashMap filters, String apiKey);

    KYCSubmissionDTO findExternalSubmissionByFormIdAndUserId(String formId, String uniqueId);
    KYCSubmissionDTO findByUserIdAndCredentialId(String userId, String credentialId);
}
