package io.proximax.kyc.repository;

import io.proximax.kyc.domain.FormIO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormIORepository extends MongoRepository<FormIO, String> {

    Page<FormIO> findAll(Pageable pageable);
}
