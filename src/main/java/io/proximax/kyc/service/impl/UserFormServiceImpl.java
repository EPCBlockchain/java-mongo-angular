package io.proximax.kyc.service.impl;

import io.proximax.kyc.service.UserFormService;
import io.proximax.kyc.domain.form.UserForm;
import io.proximax.kyc.repository.UserFormRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing Form.
 */
@Service
public class UserFormServiceImpl implements UserFormService {

    private final Logger log = LoggerFactory.getLogger(UserFormServiceImpl.class);

    private final UserFormRepository userFormRepository;

    public UserFormServiceImpl(UserFormRepository userFormRepository) {
        this.userFormRepository = userFormRepository;
    }

    @Override
    public UserForm save(UserForm Form) {
        log.debug("Request to save Form : {}", Form);
        return userFormRepository.save(Form);
    }

    @Override
    public List<UserForm> findAll() {
        log.debug("Request to get all Forms");
        return userFormRepository.findAll();
    }

    @Override
    public Optional<UserForm> findOne(String id) {
        log.debug("Request to get Form : {}", id);
        return userFormRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Form : {}", id);
        userFormRepository.deleteById(id);
    }

    @Override
    public UserForm findOneByUserIdAndFormId(String userId, String formId) {
        log.debug("Request to get Form : {}", userId);
        return userFormRepository.findOneByUserIdAndFormId(userId, formId);
    }

    @Override
    public Page<UserForm> findAllByUserId(String userId, Pageable page) {
        log.debug("Request to get Form : {}", userId);
        return userFormRepository.findAllByUserId(userId, page);
    }

    @Override
    public Page<UserForm> findAllByUserIdAndStatus(String userId, String status, Pageable page) {
        log.debug("Request to get Form : {}", userId);
        return userFormRepository.findAllByUserIdAndStatus(userId, status, page);
    }

    @Override
    public Optional<UserForm> findOneByFormIdAndViewers(String formId, String organizationId) {
        return null;
//        return userFormRepository.findOneByFormIdAndViewers(formId, organizationId);
    }

    @Override
    public Optional<UserForm> findOneByUserIdAndFormIdAndViewers(String userId, String formId, String organizationId) {
        return null;
//        return userFormRepository.findOneByUserIdAndFormIdAndViewers(userId, formId, organizationId);
    }
}
