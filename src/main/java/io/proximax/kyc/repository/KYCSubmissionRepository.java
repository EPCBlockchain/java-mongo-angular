package io.proximax.kyc.repository;

import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.service.dto.KYCSubmissionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KYCSubmissionRepository extends MongoRepository<KYCSubmission, String> {
    Long countByFormId(String formId);
    KYCSubmission findOneByUserIdAndFormId(String userId, String formId);
    Page<KYCSubmission> findAllByFormIdAndStatusNot(String formId, String status, Pageable page);
    Page<KYCSubmission> findAllByFormIdAndStatusInOrderByCreationDateDesc(String formId, List<String> status, Pageable page);
    KYCSubmission findOneByUserIdAndFormIdAndStatusNot(String userId, String formId, String status);
    Page<KYCSubmission> findAllByUserIdAndStatus(String userId, String status, Pageable page);
    Page<KYCSubmission> findAllByUserIdAndStatusInOrderByCreationDateDesc(String userId, List<String> status, Pageable page);

    Page<KYCSubmission> findAllByFormIdAndStatusInAndFilterHashesMatches(String formId, List status, List filters, Pageable page);
    @Query("{ 'form_id': ?0, 'filter_hashes' : { $all: ?1 } }")
    Page<KYCSubmission> findAllByFormIdAndFilterHashesAndStatusNotInOrderByCreationDateDesc(String formId, List filters, List<String> status, Pageable page);

    @Query("{ 'user_id': ?0, 'filter_hashes' : { $all: ?1 }, 'status' : { $in: ?2 } }")
    Page<KYCSubmission> findAllKYCSubmissionsByUserIdAndFilterHashesInAndStatusInOrderByCreationDateDesc(String userId, List filters, List status, Pageable page);

    Page<KYCSubmission> findAllByFormIdInAndStatusIn(String formId, List<String> status, Pageable page);
    @Query("{ 'form_id' : { $all: ?0 } }, 'filter_hashes' : { $all: ?1 } }")
    Page<KYCSubmission> findAllByFormIdInAndByFilterHashes(List<String> versionIds, List filters, Pageable page);
    Page<KYCSubmission> findAllByFormIdInAndStatusIn(List<String> versionIds, List<String> status, Pageable page);
    List<KYCSubmission> findByFormIdAndFilterHashes(String formId, List filters);
    List<KYCSubmission> findByFormId(String formId);
    List<KYCSubmission> findByFormIdAndUserId(String formId, String userId);
    Page<KYCSubmission> findAllByFormIdAndStatusNotInOrderByCreationDateDesc(String formId, List<String> status, Pageable page);
    @Query("{ 'userId': ?0, 'filter_hashes' : { $all: ?1 } }")
    List<KYCSubmission> findAllByUserIdAndFilterHashes(String userId, List<String> filters);
    List<KYCSubmission> findAllByUserId(String userId);

    Optional<KYCSubmission> getTopByFormIdAndUserIdOrderByCreationDateDesc(String formId, String userId);
    Optional<KYCSubmission> findByUserIdAndCredentialId(String userId, String credentialId);
}
