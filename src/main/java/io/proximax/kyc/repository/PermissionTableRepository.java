package io.proximax.kyc.repository;

import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.PermissionTable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data MongoDB repository for the User entity.
 */
@Repository
public interface PermissionTableRepository extends MongoRepository<PermissionTable, String> {
    Optional<PermissionTable> findOneByUserIdAndRequesterId(String userId, String requesterId);
    Optional<PermissionTable> findOneByStatus(String status);
    Optional<PermissionTable> findOneByUserIdAndRequesterIdAndOwnerId(String userId, String requesterId, String OwnerId);
}
