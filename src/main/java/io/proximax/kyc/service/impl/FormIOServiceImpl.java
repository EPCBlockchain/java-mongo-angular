package io.proximax.kyc.service.impl;

import io.proximax.kyc.service.FormIOService;
import io.proximax.kyc.domain.FormIO;
import io.proximax.kyc.repository.FormIORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing FormIO.
 */
@Service
public class FormIOServiceImpl implements FormIOService {

    private final Logger log = LoggerFactory.getLogger(FormIOServiceImpl.class);

    private final FormIORepository formIORepository;

    public FormIOServiceImpl(FormIORepository formIORepository) {
        this.formIORepository = formIORepository;
    }

    /**
     * Save a formIO.
     *
     * @param formIO the entity to save
     * @return the persisted entity
     */
    @Override
    public FormIO save(FormIO formIO) {
        log.debug("Request to save FormIO : {}", formIO);        return formIORepository.save(formIO);
    }

    /**
     * Get all the formIOs.
     *
     * @return the list of entities
     */
    @Override
    public List<FormIO> findAll() {
        log.debug("Request to get all Forms");
        return formIORepository.findAll();
    }


    /**
     * Get one form by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<FormIO> findOne(String id) {
        log.debug("Request to get Form : {}", id);
        return formIORepository.findById(id);
    }

    /**
     * Delete the formIO by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Form : {}", id);
        formIORepository.deleteById(id);
    }

    @Override    
    public Page<FormIO> getAllForms(Pageable pageable) {
        return formIORepository.findAll(pageable);
    }
}