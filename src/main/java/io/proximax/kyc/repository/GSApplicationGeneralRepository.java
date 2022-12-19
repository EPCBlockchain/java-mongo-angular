package io.proximax.kyc.repository;
       
import io.proximax.kyc.domain.globalsettings.GSApplicationGeneral; // model
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
public interface GSApplicationGeneralRepository {

    /**
    * Find a users list     
    * @return
    */
    // Optional<GSApplicationGeneral> findAll();
    List <GSApplicationGeneral> findAll();

    /**
     * Find a user
     * @param id
     * @return
     */
    // public GSApplicationGeneral findById(String id);

    /**
     * save a new user
     * @param gsag
     * @return
     */
    public GSApplicationGeneral saveGSAG(GSApplicationGeneral gsag);

    /**
     * Update a user
     * @param gsag
     */
    public void updateGSAG(GSApplicationGeneral gsag);

    /**
     * Delete a user by id
     * @param id
     */
    public void deleteGSAG(String id);


}
