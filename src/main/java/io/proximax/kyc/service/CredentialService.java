package io.proximax.kyc.service;

import io.proximax.kyc.domain.Credential;
import io.proximax.kyc.domain.FormIO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Form.
 */
public interface CredentialService {
    Credential save(Credential credential);
    List<Credential> findAll();
    Optional<Credential> findOne(String id);
    void delete(String id);
    Page<Credential> page(Pageable pageable);
    Page<Credential> page(String organizationId, Pageable pageable);

    List<Credential> findByNameLike(String name);

    Optional<Credential> findByCode(String code);
}
