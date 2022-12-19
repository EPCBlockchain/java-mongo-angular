package io.proximax.kyc.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.proximax.kyc.domain.SubmittedForm;
import io.proximax.kyc.repository.SubmittedFormRepository;
import io.proximax.kyc.service.SubmittedFormService;

@Service
public class SubmittedFormServiceImpl implements SubmittedFormService {
    
    private final Logger log = LoggerFactory.getLogger(SubmittedFormServiceImpl.class);

    private final SubmittedFormRepository submittedFormRepository;

    public SubmittedFormServiceImpl(SubmittedFormRepository submittedFormRepository) {
        this.submittedFormRepository = submittedFormRepository;
    }

    @Override
    public SubmittedForm save(SubmittedForm submittedForm) {
        log.debug("Request to save Form : {}", submittedForm);        
        return submittedFormRepository.save(submittedForm);
    }

    
    @Override
    public List<SubmittedForm> findAll() { 
        log.debug("Request to get all submitted forms");        
        return submittedFormRepository.findAll();
    }

    
    @Override
    public Optional<SubmittedForm> findOne(String id) {
        log.debug("Request to get Form : {}", id);
        return submittedFormRepository.findById(id);
    }

    @Override
    public SubmittedForm findOneByFormId(String id) {
        log.debug("Request to get Form : {}", id);
        return submittedFormRepository.findByFormId(id);
    }

    
    @Override
    public void delete(String id) {
        log.debug("Request to delete Form : {}", id);
        submittedFormRepository.deleteById(id);
    }

    @Override
    public Page<SubmittedForm> findAllByUserId(String userId, Pageable pageable) {
        return submittedFormRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<SubmittedForm> findAllByFormId(String userId, Pageable pageable) {
        return submittedFormRepository.findAllByFormId(userId, pageable);
    }

    @Override
    public Page<SubmittedForm> findAllByStatus(String status, Pageable pageable) {
        return submittedFormRepository.findAllByStatus(status, pageable);
    }

    @Override
    public Page<SubmittedForm> findAll(Pageable pageable) {
        return submittedFormRepository.findAll(pageable);
    }

    @Override
    public Page<SubmittedForm> findByOrganizationIdAndStatus(String orgId, String status, Pageable pageable) {
        return submittedFormRepository.findByOrganizationIdAndStatus(orgId, status, pageable);
    }
    
    @Override
    public Page<SubmittedForm> findByFormIdAndStatus(String formId, String status, Pageable pageable) {
        return submittedFormRepository.findByFormIdAndStatus(formId, status, pageable);
    }

    @Override
    public Page<SubmittedForm> findByOrganizationIdAndFormId(String organizationId, String formId, Pageable page) {
        return submittedFormRepository.findByOrganizationIdAndFormId(organizationId, formId, page);
    }

    @Override
    public Optional<SubmittedForm> findOneByFormIdAndSubmissionReferenceId(String formId, String referenceFormId) {
        return submittedFormRepository.findOneByFormIdAndSubmissionReferenceId(formId, referenceFormId);
    }

    @Override
    public Long countByFormId(String formId) {
        return submittedFormRepository.countByFormId(formId);
    }

    @Override
    public Page<SubmittedForm> findByOrganizationIdAndFormIdAndStatusNot(String orgId,String formId, String status, Pageable pageable) {
        return submittedFormRepository.findByOrganizationIdAndFormIdAndStatusNot(orgId, formId, status, pageable);
    }
    
    @Override
    public List<SubmittedForm> findAllByOrganizationIdAndFormId(String organizationId,String formId) { 
        log.debug("findAllByOrganizationIdAndFormId");        
        return submittedFormRepository.findAllByOrganizationIdAndFormId(organizationId, formId);
    }
    @Override
    public List<SubmittedForm> findAllByOrganizationIdAndFormIdAndStatusNot(String orgId, String formId, String status) { 
        log.debug("findAllByOrganizationIdAndFormIdAndStatusNot");        
        return submittedFormRepository.findAllByOrganizationIdAndFormIdAndStatusNot(orgId, formId, status);
    }
}