package io.proximax.kyc.repository;

import io.proximax.kyc.domain.OrganizationSubmittedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationSubmittedFormRepository extends MongoRepository<OrganizationSubmittedForm, String> {
    Page<OrganizationSubmittedForm> findAll(Pageable pageable);
    // OrganizationSubmittedForm findAllByOrganizationId(String organizationId);
    Page<OrganizationSubmittedForm> findAllByOrganizationId(String organizationId, Pageable page);
}
