package io.proximax.kyc.repository;
       
import io.proximax.kyc.domain.AG; // Model
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.Instant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AGRepository extends MongoRepository < AG, String > {
 AG findBy_id(ObjectId _id);
}