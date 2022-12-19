package io.proximax.kyc.repository;

    import io.proximax.kyc.domain.mongo.Form;

    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.mongodb.repository.MongoRepository;
    import org.springframework.data.mongodb.repository.Query;
    import org.springframework.stereotype.Repository;

    import javax.annotation.Nullable;
    import java.time.Instant;
    import java.util.List;
    import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormRepository extends MongoRepository<Form, String> {
    @Query(fields = "{ 'components' : 0  }")
    Form findOneByOrganizationIdAndTags(String orgId, String tag);
    Page<Form> findAllByOrganizationId(Pageable page, String organizationId);
    @Query(fields="{ '_id' : 1}")
    List<Form> findAllByVersionDetail_Code(String Code);
    Page<Form> findAllByOrganizationIdAndVersionDetail_VersionCreatedIsNullAndTagsContains(String orgId, String tag, Pageable pageable);
    @Query(fields="{ 'title' : 1}")
    Optional<Form> findTitleById(String id);

    // TODO merge

    @Query(fields = "{ 'components' : 0  }")
    Page<Form> findByOrganizationIdAndTagsInAndId(String orgId, List<String> tags, String id, Pageable pageable);
    @Query(fields = "{ 'components' : 0  }")
    Page<Form> findByOrganizationIdAndTagsInAndIdAndTitleContainingAllIgnoreCase(String orgId, List<String> tags, String id, String title, Pageable pageable);
    @Query(fields = "{ 'components' : 0  }")
    Page<Form> findByOrganizationIdAndTagsInAndIdAndTitleContainingIgnoreCaseAndCreationDate(String orgId, List<String> tags, String id, String title, String creationDate, Pageable pageable);
    @Query(fields = "{ 'components' : 0  }")
    Page<Form> findByOrganizationIdAndTagsInAndTitleContainingIgnoreCaseAndCreationDate(String orgId, List<String> tags, String title, String creationDate, Pageable pageable);
    @Query(fields = "{ 'components' : 0  }")
    Page<Form> findByOrganizationIdAndTagsInAndCreationDate(String orgId, List<String> tags, String creationDate, Pageable pageable);
    @Query(fields = "{ 'components' : 0  }")
    Page<Form> findByOrganizationIdAndTagsInAndIdAndCreationDate(String orgId, List<String> tags, String id, String creationDate, Pageable pageable);
    @Query(fields = "{ 'components' : 0  }")
    Page<Form> findByOrganizationIdAndTagsInAndTitleContainingIgnoreCase(String orgId, List<String> tags, String title, Pageable pageable);
    @Query(fields = "{ 'components' : 0  }")
    Page<Form> findByOrganizationIdAndTagsIn(String orgId, List<String> tags, Pageable pageable);

}
