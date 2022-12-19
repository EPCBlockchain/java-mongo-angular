package io.proximax.kyc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.proximax.kyc.domain.OrganizationSubmittedForm;
// import io.xpx.forms.domain.UserFormIO;

public interface OrganizationSubmittedFormService {
    
    OrganizationSubmittedForm save(OrganizationSubmittedForm form);

    List<OrganizationSubmittedForm> findAll();

    Page<OrganizationSubmittedForm> findAll(Pageable pageable);

    Optional<OrganizationSubmittedForm> findOne(String id);

    // List<OrganizationForm> findAllByFormId(String id);

    void delete(String id);

    // List<OrganizationForm> findAllByUserId(String id);

    // List<OrganizationForm> findAllByRequestor(String id);

    //Optional<OrganizationForm> findOneByOrganizationIdAndForm_Id(String userId, String formId);

    Page<OrganizationSubmittedForm> findAllByOrganizationId(String organizationId, Pageable pageable);
    // OrganizationSubmittedForm findAllByOrganizationId(String userId);
}