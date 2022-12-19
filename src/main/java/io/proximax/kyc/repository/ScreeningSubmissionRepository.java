package io.proximax.kyc.repository;

import io.proximax.kyc.domain.mongo.ScreeningSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Form entity.
 */
@Repository
public interface ScreeningSubmissionRepository extends MongoRepository<ScreeningSubmission, String> {
    Long countByFormId(String formId);
    Page<ScreeningSubmission> findAllByFormIdAndStatusNotIn(String formId, List<String> status, Pageable page);
    Page<ScreeningSubmission> findAllByFilterHashesIn(List<String> filterHashes, Pageable page);
    @Query("{ 'form_id': ?0, 'filter_hashes' : { $all: ?1 } }")
    Page<ScreeningSubmission> findByFormIdAndFilterHashesInOrderByCreationDateDesc(String formId, List<String> filterHashes, Pageable page);
    
    @Query("{ 'form_id': ?0, 'filter_hashes' : { $all: ?1 } }")
    Page<ScreeningSubmission> findAllByFormIdAndFilterHashesAndStatusNotIn(String formId, List<String> filterHashes, List<String> status, Pageable page);
    Optional<ScreeningSubmission> findBySubmissionReferenceId(String referenceId);
    
    List<ScreeningSubmission> findByFormIdAndFilterHashesIn(String formId, List filters); 
    List<ScreeningSubmission> findByFormId(String formId); 
    Page<ScreeningSubmission> findByFormIdOrderByCreationDateDesc(String formId, Pageable page); 
}
