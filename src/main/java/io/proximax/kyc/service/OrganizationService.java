package io.proximax.kyc.service;

import io.proximax.kyc.domain.Organization;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

/**
 * Service Interface for managing Form.
 */
public interface OrganizationService {

    /**
     * Save a form.
     *
     * @param organization the entity to save
     * @return the persisted entity
     */
    Organization save(Organization organization);

    /**
     * Get all the forms.
     *
     * @return the list of entities
     */
    List<Organization> findAll();


    /**
     * Get the "id" form.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Organization> findOne(String id);

    /**
     * Delete the "id" form.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    Page<Organization> getAllOrganizations(Pageable pageable);

    Boolean validateRequest(Organization organization, HttpServletRequest request);

    Optional<Organization> findOneByApiKey(String apiKey);
}
