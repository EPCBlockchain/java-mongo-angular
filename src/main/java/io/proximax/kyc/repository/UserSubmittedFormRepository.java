package io.proximax.kyc.repository;

import io.proximax.kyc.domain.UserSubmittedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSubmittedFormRepository extends MongoRepository<UserSubmittedForm, String> {
    Page<UserSubmittedForm> findAll(Pageable pageable);
    Page<UserSubmittedForm> findAllByUserId(String userId, Pageable page);
}
