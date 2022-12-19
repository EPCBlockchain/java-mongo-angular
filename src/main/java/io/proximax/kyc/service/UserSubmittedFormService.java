package io.proximax.kyc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.proximax.kyc.domain.UserSubmittedForm;
// import io.xpx.forms.domain.UserFormIO;

public interface UserSubmittedFormService {
    
    UserSubmittedForm save(UserSubmittedForm form);

    List<UserSubmittedForm> findAll();

    Page<UserSubmittedForm> findAll(Pageable pageable);

    Optional<UserSubmittedForm> findOne(String id);

    // List<OrganizationForm> findAllByFormId(String id);

    void delete(String id);

    // List<OrganizationForm> findAllByUserId(String id);

    // List<OrganizationForm> findAllByRequestor(String id);

    //Optional<OrganizationForm> findOneByOrganizationIdAndForm_Id(String userId, String formId);

    Page<UserSubmittedForm> findAllByUserId(String userId, Pageable pageable);

}