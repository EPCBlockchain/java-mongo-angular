package io.proximax.kyc.repository;

import io.proximax.kyc.domain.globalsettings.GlobalSettings;
import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.service.dto.KYCSubmissionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GlobalSettingsRepository extends MongoRepository<GlobalSettings, String> {
}
