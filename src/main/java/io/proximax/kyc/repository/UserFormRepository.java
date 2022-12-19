package io.proximax.kyc.repository;

import io.proximax.kyc.domain.form.UserForm;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the User entity.
 */
@Repository
public interface UserFormRepository extends MongoRepository<UserForm, String> {

   UserForm findOneByUserIdAndFormId(String userId, String formId);

   Page<UserForm> findAllByUserId(String userId, Pageable page);
   Page<UserForm> findAllByUserIdAndStatus(String userId, String status, Pageable page);
//   Optional<UserForm> findOneByFormIdAndViewers(String formId, String organizationId);
//   Optional<UserForm> findOneByUserIdAndFormIdAndViewers(String userId, String formId, String organizationId);
}
