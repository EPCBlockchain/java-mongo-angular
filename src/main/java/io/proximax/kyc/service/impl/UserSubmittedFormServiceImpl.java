package io.proximax.kyc.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.proximax.kyc.domain.UserSubmittedForm;
import io.proximax.kyc.repository.UserSubmittedFormRepository;
import io.proximax.kyc.service.UserSubmittedFormService;

@Service
public class UserSubmittedFormServiceImpl implements UserSubmittedFormService {
    
    private final Logger log = LoggerFactory.getLogger(UserSubmittedFormServiceImpl.class);

    private final UserSubmittedFormRepository userSubmittedFormRepository;

    
    public UserSubmittedFormServiceImpl(UserSubmittedFormRepository userSubmittedFormRepository) {
        this.userSubmittedFormRepository = userSubmittedFormRepository;
    }

    @Override
    public UserSubmittedForm save(UserSubmittedForm userSubmittedForm) {
        log.debug("Request to save Form : {}", userSubmittedForm);        
        return userSubmittedFormRepository.save(userSubmittedForm);
    }

    
    @Override
    public List<UserSubmittedForm> findAll() { 
        log.debug("Request to get all user forms");        
        return userSubmittedFormRepository.findAll();
    }

    
    @Override
    public Optional<UserSubmittedForm> findOne(String id) {
        log.debug("Request to get Form : {}", id);
        return userSubmittedFormRepository.findById(id);
    }

    
    @Override
    public void delete(String id) {
        log.debug("Request to delete Form : {}", id);
        userSubmittedFormRepository.deleteById(id);
    }

    @Override
    public Page<UserSubmittedForm> findAllByUserId(String userId, Pageable pageable) {
        return userSubmittedFormRepository.findAllByUserId(userId, pageable);
    }
    @Override
    public Page<UserSubmittedForm> findAll(Pageable pageable) {
        return userSubmittedFormRepository.findAll(pageable);
    }
}