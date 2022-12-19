package io.proximax.kyc.repository;

import io.proximax.kyc.domain.ProcessedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessedFormRepository extends MongoRepository<ProcessedForm, String> {
    Page<ProcessedForm> findAll(Pageable pageable);
    Page<ProcessedForm> findAllByUserId(String userId, Pageable page);
    Page<ProcessedForm> findAllByFormId(String formId, Pageable page);
    Page<ProcessedForm> findAllByStatus(String status, Pageable page);
}
