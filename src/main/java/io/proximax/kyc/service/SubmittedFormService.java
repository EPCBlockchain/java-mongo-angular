package io.proximax.kyc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.proximax.kyc.domain.SubmittedForm;
// import io.xpx.forms.domain.UserFormIO;

public interface SubmittedFormService {
    
    SubmittedForm save(SubmittedForm form);

    List<SubmittedForm> findAll();
    List<SubmittedForm> findAllByOrganizationIdAndFormId(String organizationId,String formId);
    List<SubmittedForm> findAllByOrganizationIdAndFormIdAndStatusNot(String orgId, String formId, String status);

    Page<SubmittedForm> findAll(Pageable pageable);

    Optional<SubmittedForm> findOne(String id);
    SubmittedForm findOneByFormId(String id);

    void delete(String id);

    Page<SubmittedForm> findAllByUserId(String userId, Pageable pageable);
    Page<SubmittedForm> findAllByFormId(String formId, Pageable pageable);
    Page<SubmittedForm> findAllByStatus(String status, Pageable pageable);

    Page<SubmittedForm> findByOrganizationIdAndStatus(String orgId, String status, Pageable pageable);
    Page<SubmittedForm> findByOrganizationIdAndFormIdAndStatusNot(String orgId, String formId, String status, Pageable pageable);
    Page<SubmittedForm> findByFormIdAndStatus(String formId, String status, Pageable pageable);    
    Page<SubmittedForm> findByOrganizationIdAndFormId(String organizationId,String formId, Pageable pageable);    

    Optional<SubmittedForm> findOneByFormIdAndSubmissionReferenceId(String formId, String referenceFormId);
    Long countByFormId(String formId);
}   