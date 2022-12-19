package io.proximax.kyc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import io.proximax.kyc.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.proximax.kyc.domain.mongo.Form;

public interface FormService {
    Form save(Form Form);
    List<Form> findAll();
    Optional<Form> findOne(String id);
    void delete(String id);

    /*
    * New services
    */

    Form createVersion(Form form, User user);
    Boolean remove(String formId, User user);
    Page<Form> findAllByOrganizationIdAndTags(String orgId, String tag, HashMap<String, String> filters, Pageable pageable);
    Page<Form> findAllbyFilters(String OrganizationId, HashMap<String, String> filters, Pageable pageable);

}
