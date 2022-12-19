package io.proximax.kyc.service.impl;

import io.proximax.kyc.service.OrganizationService;
import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing Organization.
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * Save a organization.
     *
     * @param organization the entity to save
     * @return the persisted entity
     */
    @Override
    public Organization save(Organization organization) {
        log.debug("Request to save Organization : {}", organization);
        return organizationRepository.save(organization);
    }

    /**
     * Get all the organizations.
     *
     * @return the list of entities
     */
    @Override
    public List<Organization> findAll() {
        log.debug("Request to get all organizations");
        return organizationRepository.findAll();
    }


    /**
     * Get one organization by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Organization> findOne(String id) {
        log.debug("Request to get Organization : {}", id);
        return organizationRepository.findById(id);
    }

    /**
     * Delete the organization by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
    }

    @Override
    public Page<Organization> getAllOrganizations(Pageable pageable) {
        return organizationRepository.findAll(pageable);
    }

    @Override
    public Optional<Organization> findOneByApiKey(String apiKey) {
        return organizationRepository.findOneBySecurity_ApiKey(apiKey);
    }

    @Override
    public Boolean validateRequest(Organization organization, HttpServletRequest request) {
        String allowedHost = organization.getSecurity().getAllowedHosts();
        if (allowedHost == null) {
            return false;
        }
        if (allowedHost.contains(request.getRemoteHost())) {
            return true;
        }
        if (allowedHost.contains(request.getRemoteAddr())) {
            return true;
        }
        return false;
    }
}
