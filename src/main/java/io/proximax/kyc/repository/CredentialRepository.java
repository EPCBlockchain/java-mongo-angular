package io.proximax.kyc.repository;

import io.proximax.kyc.domain.Credential;
import io.proximax.kyc.domain.mongo.Form;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Credential entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CredentialRepository extends MongoRepository<Credential, String> {
    Page<Credential> findAllByOrganizationId(String organizationId, Pageable pageable);
    List<Credential> findByNameContains(String name);
    Optional<Credential> findByCode(String code);
}
