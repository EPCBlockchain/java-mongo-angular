package io.proximax.kyc.repository;

import io.proximax.kyc.domain.Organization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Organization entity.
 */
@Repository
public interface OrganizationRepository extends MongoRepository<Organization, String> {

    Page<Organization> findAll(Pageable pageable);

    Optional<Organization> findOneBySecurity_ApiKey(String apiKey);
    
    @Query("{ 'name':{$regex:?0, $options:'i'} }") 
    Optional<Organization> findOneByName(String name);
}
