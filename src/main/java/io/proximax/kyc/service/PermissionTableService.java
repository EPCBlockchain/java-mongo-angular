package io.proximax.kyc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import io.proximax.kyc.domain.PermissionTable;
import io.proximax.kyc.domain.form.UserForm;

public interface PermissionTableService {
    PermissionTable save(PermissionTable form);
    List<PermissionTable> findAll();
    Optional<PermissionTable> findOne(String id);
    void delete(String id);
    Optional<PermissionTable> findOneByUserIdAndRequesterId(String userId, String requesterId);
    Optional<PermissionTable> findOneByStatus(String status);
    Optional<PermissionTable> findOneByUserIdAndRequesterIdAndOwnerId(String userId, String requesterId, String OwnerId);
    // Page<UserForm> findAllByUserId(String userId, Pageable page);
    // Page<UserForm> findAllByUserIdAndStatus(String userId, String status, Pageable page);
}
