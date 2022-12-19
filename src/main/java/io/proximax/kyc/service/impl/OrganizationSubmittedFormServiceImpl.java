package io.proximax.kyc.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.proximax.kyc.domain.OrganizationSubmittedForm;
import io.proximax.kyc.repository.OrganizationSubmittedFormRepository;
import io.proximax.kyc.service.OrganizationSubmittedFormService;

@Service
public class OrganizationSubmittedFormServiceImpl implements OrganizationSubmittedFormService {
    
    private final Logger log = LoggerFactory.getLogger(OrganizationSubmittedFormServiceImpl.class);

    private final OrganizationSubmittedFormRepository organizationSubmittedFormRepository;

    
    public OrganizationSubmittedFormServiceImpl(OrganizationSubmittedFormRepository organizationSubmittedFormRepository) {
        this.organizationSubmittedFormRepository = organizationSubmittedFormRepository;
    }

    @Override
    public OrganizationSubmittedForm save(OrganizationSubmittedForm organizationSubmittedForm) {
        log.debug("Request to save Form : {}", organizationSubmittedForm);        
        return organizationSubmittedFormRepository.save(organizationSubmittedForm);
    }

    
    @Override
    public List<OrganizationSubmittedForm> findAll() { 
        log.debug("Request to get all user forms");        
        return organizationSubmittedFormRepository.findAll();
    }

    
    @Override
    public Optional<OrganizationSubmittedForm> findOne(String id) {
        log.debug("Request to get Form : {}", id);
        return organizationSubmittedFormRepository.findById(id);
    }

    
    @Override
    public void delete(String id) {
        log.debug("Request to delete Form : {}", id);
        organizationSubmittedFormRepository.deleteById(id);
    }

    @Override
    public Page<OrganizationSubmittedForm> findAllByOrganizationId(String organizationId, Pageable pageable) {
        return organizationSubmittedFormRepository.findAllByOrganizationId(organizationId, pageable);
    }
    @Override
    public Page<OrganizationSubmittedForm> findAll(Pageable pageable) {
        return organizationSubmittedFormRepository.findAll(pageable);
    }
}