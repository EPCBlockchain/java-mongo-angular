package io.proximax.kyc.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.proximax.kyc.domain.ProcessedForm;
import io.proximax.kyc.repository.ProcessedFormRepository;
import io.proximax.kyc.service.ProcessedFormService;

@Service
public class ProcessedFormServiceImpl implements ProcessedFormService {
    
    private final Logger log = LoggerFactory.getLogger(ProcessedFormServiceImpl.class);

    private final ProcessedFormRepository processedFormRepository;

    public ProcessedFormServiceImpl(ProcessedFormRepository processedFormRepository) {
        this.processedFormRepository = processedFormRepository;
    }

    @Override
    public ProcessedForm save(ProcessedForm processedForm) {
        log.debug("Request to save Form : {}", processedForm);        
        return processedFormRepository.save(processedForm);
    }

    
    @Override
    public List<ProcessedForm> findAll() { 
        log.debug("Request to get all submitted forms");        
        return processedFormRepository.findAll();
    }

    
    @Override
    public Optional<ProcessedForm> findOne(String id) {
        log.debug("Request to get Form : {}", id);
        return processedFormRepository.findById(id);
    }

    
    @Override
    public void delete(String id) {
        log.debug("Request to delete Form : {}", id);
        processedFormRepository.deleteById(id);
    }

    @Override
    public Page<ProcessedForm> findAllByUserId(String userId, Pageable pageable) {
        return processedFormRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<ProcessedForm> findAllByFormId(String userId, Pageable pageable) {
        return processedFormRepository.findAllByFormId(userId, pageable);
    }

    @Override
    public Page<ProcessedForm> findAllByStatus(String status, Pageable pageable) {
        return processedFormRepository.findAllByStatus(status, pageable);
    }

    @Override
    public Page<ProcessedForm> findAll(Pageable pageable) {
        return processedFormRepository.findAll(pageable);
    }
}