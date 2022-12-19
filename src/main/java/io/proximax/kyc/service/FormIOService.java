package io.proximax.kyc.service;

import io.proximax.kyc.domain.FormIO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Form.
 */
public interface FormIOService {

    /**
     * Save a form.
     *
     * @param formIO the entity to save
     * @return the persisted entity
     */
    FormIO save(FormIO formIO);

    /**
     * Get all the forms.
     *
     * @return the list of entities
     */
    List<FormIO> findAll();


    /**
     * Get the "id" form.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FormIO> findOne(String id);

    /**
     * Delete the "id" form.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    Page<FormIO> getAllForms(Pageable pageable);
}
