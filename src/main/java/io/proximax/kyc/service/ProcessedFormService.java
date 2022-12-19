package io.proximax.kyc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.proximax.kyc.domain.ProcessedForm;
// import io.xpx.forms.domain.UserFormIO;

public interface ProcessedFormService {
    
    ProcessedForm save(ProcessedForm form);

    List<ProcessedForm> findAll();

    Page<ProcessedForm> findAll(Pageable pageable);

    Optional<ProcessedForm> findOne(String id);

    void delete(String id);

    Page<ProcessedForm> findAllByUserId(String userId, Pageable pageable);
    Page<ProcessedForm> findAllByFormId(String formId, Pageable pageable);
    Page<ProcessedForm> findAllByStatus(String status, Pageable pageable);
}