package io.proximax.kyc.repository;

import io.proximax.kyc.domain.SubmittedForm;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubmittedFormRepository extends MongoRepository<SubmittedForm, String> {
    Page<SubmittedForm> findAll(Pageable pageable);
    Page<SubmittedForm> findAllByUserId(String userId, Pageable page);
    Page<SubmittedForm> findAllByFormId(String formId, Pageable page);
    Page<SubmittedForm> findAllByStatus(String status, Pageable page);
    SubmittedForm findByFormId(String formId);
    Page<SubmittedForm> findByOrganizationId(String orgId, Pageable page);
    Page<SubmittedForm> findByOrganizationIdAndStatus(String orgId, String status, Pageable page);  
    Page<SubmittedForm> findByFormIdAndStatus(String formId, String status, Pageable page); 
    Page<SubmittedForm> findByOrganizationIdAndFormId(String organizationId, String formId, Pageable page); 
    Optional<SubmittedForm> findOneByFormIdAndSubmissionReferenceId(String formId, String referenceFormId);
    Page<SubmittedForm> findByOrganizationIdAndFormIdAndStatusNot(String orgId,String formId, String status, Pageable pageable);
    Long countByFormId(String formId);
    
    List<SubmittedForm> findAllByOrganizationIdAndFormId(String organizationId, String formId); 
    List<SubmittedForm> findAllByOrganizationIdAndFormIdAndStatusNot(String organizationId, String formId,String status ); 
}
