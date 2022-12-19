package io.proximax.kyc.service.impl;

import io.proximax.kyc.service.PermissionTableService;
import io.proximax.kyc.domain.PermissionTable;
import io.proximax.kyc.repository.PermissionTableRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing Form.
 */
@Service
public class PermissionTableServiceImpl implements PermissionTableService {

    private final Logger log = LoggerFactory.getLogger(UserFormServiceImpl.class);

    private final PermissionTableRepository permissionTableRepository;

    public PermissionTableServiceImpl(PermissionTableRepository permissionTableRepository) {
        this.permissionTableRepository = permissionTableRepository;
    }

    @Override
    public PermissionTable save(PermissionTable form) {
        log.debug("Request to save permission record : {}", form);
        return permissionTableRepository.save(form);
    }

    @Override
    public List<PermissionTable> findAll() {
        log.debug("Request to get all permission records");
        return permissionTableRepository.findAll();
    }

    @Override
    public Optional<PermissionTable> findOne(String id) {
        log.debug("Request to get permission record : {}", id);
        return permissionTableRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete permission record : {}", id);
        permissionTableRepository.deleteById(id);
	}

    @Override
    public Optional<PermissionTable> findOneByUserIdAndRequesterId(String userId, String requesterId) {
        log.debug("Request to check Form : {}",userId);
        return permissionTableRepository.findOneByUserIdAndRequesterId(userId, requesterId);
    }

    @Override
    public Optional<PermissionTable> findOneByUserIdAndRequesterIdAndOwnerId(String userId, String requesterId, String ownerId) {
        log.debug("Request to check Form : {}",userId);
        return permissionTableRepository.findOneByUserIdAndRequesterIdAndOwnerId(userId, requesterId, ownerId);
    }
    @Override
    public Optional<PermissionTable> findOneByStatus(String status) {
        log.debug("Request to get Form by status : {}",status);
        return permissionTableRepository.findOneByStatus(status);
    }
}
