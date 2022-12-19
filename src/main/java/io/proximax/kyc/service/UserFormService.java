package io.proximax.kyc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.proximax.kyc.domain.form.UserForm;

public interface UserFormService {
    UserForm save(UserForm Form);
    List<UserForm> findAll();
    Optional<UserForm> findOne(String id);
    void delete(String id);
    
    UserForm findOneByUserIdAndFormId(String userId, String FormId);
    Page<UserForm> findAllByUserId(String userId, Pageable page);
    Page<UserForm> findAllByUserIdAndStatus(String userId, String status, Pageable page);

    Optional<UserForm> findOneByFormIdAndViewers(String formId, String organizationId);
    Optional<UserForm> findOneByUserIdAndFormIdAndViewers(String userId, String formId, String organizationId);
}
