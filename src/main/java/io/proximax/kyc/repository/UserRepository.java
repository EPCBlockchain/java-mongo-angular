package io.proximax.kyc.repository;

import io.proximax.kyc.domain.User;

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
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    List<User> findByIdIn(List<String> userIds);

    Optional<User> findOneByLogin(String login);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    Page<User> findAllByLoginLike(Pageable pageable, String login);

    Page<User> findAllByOrganizationId(String organizationId, Pageable pageable);

    List<User> findAllListByOrganizationId(String organizationId);

    List<User> findAll();
}
