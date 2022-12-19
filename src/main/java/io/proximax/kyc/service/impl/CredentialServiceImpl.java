package io.proximax.kyc.service.impl;

import io.proximax.kyc.domain.Credential;
import io.proximax.kyc.repository.CredentialRepository;
import io.proximax.kyc.service.CredentialService;
import io.proximax.kyc.service.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing Form.
 */
@Service
public class
CredentialServiceImpl implements CredentialService {

    private final Logger log = LoggerFactory.getLogger(CredentialServiceImpl.class);

    private final CredentialRepository credentialRepository;
    private final StorageService storageService;

    public CredentialServiceImpl(CredentialRepository credentialRepository, StorageService storageService) {
        this.credentialRepository = credentialRepository;
        this.storageService = storageService;
    }

    @Override
    public Credential save(Credential credential) {
        String hash = null;
        if (credential.getImageFile() != null) {
            hash = this.storageService.store(credential.getImageFile());
            credential.setImage(hash);
            credential.setImageFile(null);
        }

        if (credential.getId() != null) {
            Optional<Credential> dbCredentialOptional = credentialRepository.findById(credential.getId());
            Credential dbCredential = dbCredentialOptional.get();
            if (hash != null) {
                dbCredential.setImage(hash);
            }
            dbCredential.setName(credential.getName());
            dbCredential.setDescription(credential.getDescription());
            return this.credentialRepository.save(dbCredential);
        }

        credential.setCreationDate(Instant.now());
        credential.setCode(UUID.randomUUID().toString());
        return this.credentialRepository.save(credential);
    }

    @Override
    public List<Credential> findAll() {
        return this.credentialRepository.findAll();
    }

    @Override
    public Optional<Credential> findOne(String id) {
        return this.credentialRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        this.credentialRepository.deleteById(id);
    }

    @Override
    public Page<Credential> page(Pageable pageable) {
        return this.credentialRepository.findAll(pageable);
    }

    @Override
    public Page<Credential> page(String organizationId, Pageable pageable) {
        return this.credentialRepository.findAllByOrganizationId(organizationId, pageable);
    }

    @Override
    public List<Credential> findByNameLike(String name) {
        return this.credentialRepository.findByNameContains(name);
    }

    @Override
    public Optional<Credential> findByCode(String code) {
        return this.credentialRepository.findByCode(code);
    }
}
